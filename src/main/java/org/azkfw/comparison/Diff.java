package org.azkfw.comparison;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.azkfw.stepcounter.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Diff {

	public Diff() {

	}

	private Logger logger = LoggerFactory.getLogger(Diff.class);

	private List<String> lineX;

	private List<String> lineY;

	public void diff(final File file1, final File file2) throws IOException {

		lineX = FileUtils.readFileToStrings(file1);
		lineY = FileUtils.readFileToStrings(file2);
		logger.debug("行数 {} x {}", lineX.size(), lineY.size());

		Step s = new Step(0, 0);
		List<Step> steps = new ArrayList<Diff.Step>();
		steps.add(s);

		for (int d = 0; d < lineX.size() + lineY.size(); d++) {
			logger.debug(" D {}", d);

			Store buffer = new Store();
			for (Step step : steps) {
				bbb(step, buffer);
			}

			steps = buffer.cleanup();

			logger.debug(" Size {} -> {}", buffer.size(), steps.size());

			int cntMatch = 0;
			for (Step step : steps) {
				if (lineX.size() == step.x && lineY.size() == step.y) {
					cntMatch++;
				}
			}
			if (0 < cntMatch) {
				logger.debug("unmatch {} {}", d, cntMatch);
				break;
			}
		}

		Step end = null;
		for (Step step : steps) {
			if (lineX.size() == step.x && lineY.size() == step.y) {
				end = step;
			}
		}

		List<Step> call = end.getStepList();
		int x = 0;
		int y = 0;
		for (Step c : call) {
			// logger.debug("{}x{}", c.x, c.y);

			int dx = c.x - x;
			int dy = c.y - y;
			int d = dx - dy;

			if (0 == d && (c.x != 0)) {
				// match
				System.out.println(String.format("%s\t%s\t%s", "", lineX.get(c.x - 1), lineY.get(c.y - 1)));

			} else if (1 == d) {
				// insert
				System.out.println(String.format("%s\t%s\t%s", "I", lineX.get(c.x - 1), ""));
			} else if (-1 == d) {
				// delete
				System.out.println(String.format("%s\t%s\t%s", "D", "", lineY.get(c.y - 1)));
			}

			x = c.x;
			y = c.y;
		}

	}

	private void bbb(final Step step, final Store store) {

		if (lineX.size() > step.x && lineY.size() > step.y) {
			String strX = lineX.get(step.x);
			String strY = lineY.get(step.y);
			if (strX.equals(strY)) {
				Step s = new Step(step, step.x + 1, step.y + 1);
				bbb(s, store);
			} else {
				Step sx = new Step(step, step.x + 1, step.y);
				store.add(sx);
				Step sy = new Step(step, step.x, step.y + 1);
				store.add(sy);
			}
		} else if (lineX.size() > step.x) {
			Step s = new Step(step, step.x + 1, step.y);
			store.add(s);
		} else if (lineY.size() > step.y) {
			Step s = new Step(step, step.x, step.y + 1);
			store.add(s);
		} else {

		}

	}

	private class Store {

		private Map<Integer, List<Step>> map;

		private int size;

		public Store() {
			map = new HashMap<Integer, List<Diff.Step>>();
			size = 0;
		}

		public void add(final Step step) {
			int k = step.x - step.y;

			size++;

			List<Step> steps = null;
			if (map.containsKey(k)) {
				steps = map.get(k);
			} else {
				steps = new ArrayList<Diff.Step>();
				map.put(k, steps);
			}

			steps.add(step);
		}

		public List<Step> cleanup() {
			List<Step> steps = new ArrayList<Diff.Step>();

			for (Integer k : map.keySet()) {

				Step tmp = null;
				List<Step> list = map.get(k);
				for (Step step : list) {
					if (null == tmp) {
						tmp = step;
					} else {
						if (step.x > tmp.x) {
							tmp = step;
						}
					}
				}

				steps.add(tmp);

			}

			return steps;
		}

		public int size() {
			return size;
		}
	}

	private class Step {

		private Step parent;

		private int x;

		private int y;

		public Step(final int x, final int y) {
			parent = null;
			this.x = x;
			this.y = y;
		}

		public Step(final Step parent, final int x, final int y) {
			this.parent = parent;
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public List<Step> getStepList() {
			List<Step> steps = new ArrayList<Diff.Step>();

			store(steps);

			return steps;
		}

		private void store(final List<Step> steps) {
			steps.add(0, this);
			if (null != parent) {
				parent.store(steps);
			}
		}
	}

}
