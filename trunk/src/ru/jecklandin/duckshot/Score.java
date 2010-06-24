package ru.jecklandin.duckshot;

class Score implements Comparable<Score>{


	public String name;
	public int score;
	
	Score(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		}
		return this.name.equals(((Score)o).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode() ^ score;
	}

	@Override
	public int compareTo(Score another) {
		if (another == null || score < another.score) {
			return -1;
		} else if (score > another.score) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return name + " " + score;
	}
	
}