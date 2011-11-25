package inc.bezdelniki.snakegame.time;

public class TimeService implements ITimeService {

	@Override
	public long getNanoStamp() {
		return System.nanoTime();
	}

}
