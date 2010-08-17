#!/usr/bin/python

import sqlite3
import sys
import csv
import numpy

def main():
  if (len(sys.argv) < 2):
    print "Usage!"
    exit(1)
  
  database = sys.argv[1]
    
  portraits = csv.reader(open('portrait.csv', 'rb'))

  alphabet = {}

  for row in portraits:
    alphabet[row[0]] = (int(row[1]), int(row[2]))
  
  conn = sqlite3.connect(sys.argv[1])
  cur = conn.cursor()

  cur.execute('select datetime, data from clues where kind="keypress"')
  keypresses = {}

  keypresstimes = []

  for row in cur:
    keypresstimes.append(int(row[0]))
    keypresses[row[0]] = row[1]

  total = 0

  for i in range(0,len(keypresstimes)-1):
    total = total + (keypresstimes[i+1]-keypresstimes[i])
  
  avgtypingspeed = total/len(keypresstimes)
  print "Average letters per second: %f" % (1000.0/avgtypingspeed)

  keytimes = {}
  times = []
  data = []

  for k,v in keypresses.iteritems():
    cur.execute('select datetime, data from clues where datetime < %d and datetime > %d and kind="%s"' % (k, k-100, "touch"))
    for row in cur:
      data = row[1].split(',')
      times.append((row[0], (int(data[0]), int(data[1])), float(data[2])))
    keytimes[v] = times
    times = []
  
  magnitudes = []

  print "Average error magnitudes for each key:"
  for k, v in sorted(keytimes.iteritems()):
    center = alphabet[k]
    for item in v:
      touch = item[1]
      magnitudes.append(pow(pow(touch[0] - center[0], 2) + pow(touch[1] - center[1], 2), 0.5))
    print "%s: %f" % (k, float(sum(magnitudes))/len(magnitudes))
    magnitudes = []
    
if __name__ == "__main__":
  sys.exit(main())
