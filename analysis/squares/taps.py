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
  
  boxData = list(csv.reader(open(points, 'rb')))

  boxes = [{"time":long(row[0]), "size":float(row[1]), "position":(int(row[2]),int(row[3]))} for row in boxData[:-1]]
  finishTime = long(boxData[-1][0].split(':')[1])
    
  conn = sqlite3.connect(database)
  cur = conn.cursor()
  cur.execute('select datetime,data from clues where kind="touch" and datetime between %d and %d' % (boxes[0]['time'],finishTime))

  touches = [{'time':long(t[0]), 'position':(int(t[1].split(',')[0]), int(t[1].split(',')[1])), 'pressure':float(t[1].split(',')[2])} for t in cur]
  
  boxesWithTouches = []
  for i,box in enumerate(boxes[:-1]):
    time = box['time']
    nextTime = boxes[i+1]['time']
    def f(x): return x['time'] > time and x['time'] < nextTime
    associatedTouches = filter(f, touches)
    numTaps = 1
    for j,touch in enumerate(associatedTouches[:-1]):
      currentTouchTime = touch['time']
      nextTouchTime = associatedTouches[j+1]['time']
      if (nextTouchTime - currentTouchTime) > 100:
        numTaps += 1
    boxesWithTouches.append({'size': box['size'], 'position': box['position'], 'touches':associatedTouches, 'attempts':numTaps})
    numTaps = 0
  
  # Finish up with the last box
  box = boxes[-1]
  time = boxes[-1]['time']
  nextTime = finishTime
  def f(x): return x['time'] > time and x['time'] < nextTime
  associatedTouches = filter(f, touches)
  numTaps = 1
  for j,touch in enumerate(associatedTouches[:-1]):
    currentTouchTime = touch['time']
    nextTouchTime = associatedTouches[j+1]['time']
    if (nextTouchTime - currentTouchTime) > 100:
      numTaps += 1
  boxesWithTouches.append({'size': box['size'], 'position': box['position'], 'touches':associatedTouches, 'attempts':numTaps})  
  
  sizes = [30, 60, 99, 129]
  mmsizes = [3,6,10,13]

  for buttonSize in sizes:
    def sizeOfBox(t): return t['size'] == buttonSize
    filteredBoxes = filter(sizeOfBox, boxesWithTouches)
    listOfAttempts = [box['attempts'] for box in filteredBoxes]
    print "%d: %.3f/%.3f/%.3f/%.3f" % (mmsizes[sizes.index(buttonSize)], stats.mean(listOfAttempts), min(listOfAttempts), max(listOfAttempts), stats.stdev(listOfAttempts))

if __name__ == "__main__":
  sys.exit(main())
