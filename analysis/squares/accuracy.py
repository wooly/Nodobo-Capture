#!/usr/bin/python

import sqlite3
import sys
import csv
import math

def main():
  # if (len(sys.argv) < 3):
  #   print "Usage!"
  #   exit(1)
  
  database = '/Users/stephen/clues.sqlite3'
  points = '/Users/stephen/20100823111432.csv'
  
  boxData = csv.reader(open(points, 'rb'))

  boxes = []

  for row in boxData:
    if (len(row) == 4):
      boxes.append({"time":long(row[0]), "size":int(row[1]), "position":((int(row[2])*3)/2,(int(row[3])*3)/2)})
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

  total = sum((box['time'] - boxes[i]['time']) for i,box in enumerate(boxes[1:]))
  average = total/80
  print "Average boxes per second: %f" % (1000.0/average)

  boxesWithTouches = []
  
  for i,box in enumerate(boxes[:-1]):
    time = box['time']
    nextTime = boxes[i+1]['time']
    def f(x): return x['time'] > time and x['time'] < nextTime
    associatedTouches = filter(f, touches)
    boxesWithTouches.append({'position': box['position'], 'touches':associatedTouches})

  mags = []

  print "Average error magnitudes for each button:"
  for boxWithTouch in boxesWithTouches:
    center = boxWithTouch['position']
    for touch in boxWithTouch['touches']:
      tapPos = touch['position']
      deltaX = center[0] - tapPos[0]
      deltaY = center[1]  - tapPos[1]
      mags.append(math.sqrt(pow(deltaX, 2) + pow(deltaY, 2)))
    print "Box: %s has error %f" % (center, sum(mags)/len(mags))
    mags = []
  

if __name__ == "__main__":
  sys.exit(main())
