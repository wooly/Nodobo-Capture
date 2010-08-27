#!/usr/bin/python

import sqlite3
import sys
import csv
import math

from statlib import stats

def main():
  if (len(sys.argv) < 2):
    print "Usage!"
    exit(1)
  
  database = '/Users/stephen/clues.sqlite3'
  points = sys.argv[1]
  
  boxData = csv.reader(open(points, 'rb'))

  boxes = []

  for row in boxData:
    if (len(row) == 4):
      boxes.append({"time":long(row[0]), "size":float(row[1]), "position":(int(row[2]),int(row[3]))})
    else:
      finishTime = long(row[0].split(':')[1])
  
  conn = sqlite3.connect(database)
  cur = conn.cursor()
  cur.execute('select datetime,data from clues where kind="touch" and datetime between %d and %d' % (boxes[0]['time'],finishTime))

  touches = []
  for touch in cur:
    time = long(touch[0])
    data = touch[1].split(',')
    touches.append({"time":long(touch[0]), "position":(int(data[0]),int(data[1])), "pressure":float(data[2])})

  timesForSize = {30:[], 60:[], 99:[], 129:[]}

  deltas = []
  for i,box in enumerate(boxes[:-1]):
    delta = (boxes[i+1]['time'] - box['time'])/1000.0
    timesForSize[box['size']].append(delta)
    deltas.append(delta)
  deltas.append((finishTime - boxes[-1]['time'])/1000.0)
  minimum = min(deltas)
  maximum = max(deltas)
  mean = stats.mean(deltas)
  stddev = stats.stdev(deltas)
  for k,v in sorted(timesForSize.iteritems()):
    print "%d: %.3f/%.3f/%.3f/%.3f (%.3f bps)" % (k, min(v), stats.mean(v), max(v), stats.stdev(v), 1/stats.mean(v))
  print "Avg: %.3f/%.3f/%.3f/%.3f (%.3f boxes per second)" % (minimum, mean, maximum, stddev, 1/mean)

  boxesWithTouches = []
  
  for i,box in enumerate(boxes[:-1]):
    time = box['time']
    nextTime = boxes[i+1]['time']
    def f(x): return x['time'] > time and x['time'] < nextTime
    associatedTouches = filter(f, touches)
    boxesWithTouches.append({'size': box['size'], 'position': box['position'], 'touches':associatedTouches})

  mags = []
  magsPerSize = []
  sizes = [30, 60, 99, 129]
  
  for buttonSize in sizes:
    def sizeOfBox(t): return t['size'] == buttonSize
    boxes = filter(sizeOfBox, boxesWithTouches)
    for boxWithTouch in boxes:
      center = boxWithTouch['position']
      for touch in boxWithTouch['touches']:
        tapPos = touch['position']
        deltaX = center[0] - tapPos[0]
        deltaY = center[1]  - tapPos[1]
        mags.append(math.sqrt(pow(deltaX, 2) + pow(deltaY, 2)))
      magsPerSize = magsPerSize + mags
      mags = []
    # print "%d: %.3f/%.3f/%.3f/%.3f" % (buttonSize, min(magsPerSize), stats.mean(magsPerSize), max(magsPerSize), stats.stdev(magsPerSize))
    magsPerSize = []
  

if __name__ == "__main__":
  sys.exit(main())
