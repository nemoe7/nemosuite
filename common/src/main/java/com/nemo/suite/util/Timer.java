package com.nemo.suite.util;

public class Timer {
  long tick = 0;

  public Timer() {
  }

  public void reset() {
    tick = 0;
  }

  public void tick() {
    tick += 1;
  }

  public void tick(int i) {
    tick += i;
  }

  public void tick(long l) {
    tick += l;
  }

  public boolean reached(int i) {
    return tick >= i;
  }

  public boolean reached(long l) {
    return tick >= l;
  }

  public long getTicks() {
    return tick;
  }
}
