#!/usr/bin/python

import sqlite3
import sys
import csv
import math

from statlib import stats

def main():
  if (len(sys.argv) < 3):
    print "Usage!"
    exit(1)
  
  database = sys.argv[2]
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
  mmsizes = {30:3, 60:6, 99:10, 129:13}

  deltas = []
  for i,box in enumerate(boxes[:-1]):
    delta = (boxes[i+1]['time'] - box['time'])/1000.0
    timesForSize[box['size']].append(delta)
    deltas.append(delta)
  deltas.append((finishTime - boxes[-1]['time'])/1000.0)
  for k,v in sorted(timesForSize.iteritems()):
    print "%d: %.3f/%.3f/%.3f/%.3f (%.3f bps)" % (mmsizes[k], stats.mean(v), min(v), max(v), stats.stdev(v), 1/stats.mean(v))
  
if __name__ == "__main__":
  sys.exit(main())
