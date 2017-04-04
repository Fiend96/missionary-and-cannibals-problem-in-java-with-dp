package darkness;

/**
 * 开船
 * 
 * @author lihanguang
 *
 */
public class Drive {
	// 传教士数量
	public int ml;
	// 野蛮人数量
	public int cl;

	public Drive() {

	}

	public Drive(int ml, int cl) {
		super();
		this.ml = ml;
		this.cl = cl;
	}

	public boolean isSafe() {
		return ml >= cl || cl == 0 || ml == 0;
	}

	public String toString() {
		return "传道士：" + ml + "，野人：" + cl + "\n";
	}

	@Override
	public int hashCode() {
		return ("ml:" + ml + ",cl:" + cl).hashCode();
	}
}
