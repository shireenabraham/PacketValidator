package com.illumio.firewall.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.lang.Comparable;

@AllArgsConstructor
public class Range<T extends Comparable<T>> implements Comparable<Range<T>> {
	private T start, end;

	/**
	* Ranges which do not overlap are not equal
	* The range having the smaller numbers come before. i.e. 
	* Used to store the elements in Red Black tree (TreeSet).
	*/
	@Override
	public int compareTo(Range<T> range) {
		T a = this.start;
		T b = this.end;
		T c = range.start;
		T d = range.end;
		
		if (this.equals(range)){
			return 0;
		}
		else if (a.compareTo(c) < 0 && b.compareTo(c) < 0){
			return -1;
		}
		else{
			return 1;
		}
	}

	/**
	* Equals is used for contains() in TreeSet.
	*/
	@Override
	public boolean equals(Object rangeObj) {
		Range<T> range = (Range<T>) rangeObj;

		T a = this.start;
		T b = this.end;
		T c = range.start;
		T d = range.end;

		return a.compareTo(c) <= 0 && b.compareTo(c) >= 0 || c.compareTo(a) <= 0 && d.compareTo(a) >= 0;
	}

	@Override
	public String toString() {
		return this.start.toString() + " " + this.end.toString();
	}

	/**
	* We merge two ranges to give us a range which is inclusive of both the ranges.
	*/
	public Range<T> merge(Range<T> range) {
		T a = this.start;
		T b = this.end;
		T c = range.start;
		T d = range.end;

		T start = (a.compareTo(c) <= 0)? a: c;
		T end = (b.compareTo(d) >= 0)? b: d;

		return new Range<T>(start, end);
	}
}