#!/usr/bin/env python
import random

num_tests = 1000000

def run_test():
  total_flips = 0
  heads = 0
  while heads < 3:
    flip = random.randint(0,1)
    if flip == 1:
      heads += 1
    else:
      heads = 0
    total_flips += 1
  return total_flips


total_flips = 0
tests_run = 0
for i in range(num_tests):
  total_flips += run_test()
  tests_run += 1.0

print str(total_flips / tests_run)

