/*
 * Andoku - a sudoku puzzle game for Android.
 * Copyright (C) 2009  Markus Wiederkehr
 *
 * This file is part of Andoku.
 *
 * Andoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Andoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Andoku.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.andoku.db;

import android.test.AndroidTestCase;

import com.googlecode.andoku.model.Difficulty;

public class AndokuDatabasePuzzleTest extends AndroidTestCase {
	private AndokuDatabase db;

	@Override
	protected void setUp() throws Exception {
		db = new AndokuDatabase(getContext());
		db.resetAll();
	}

	@Override
	protected void tearDown() throws Exception {
		db.close();
	}

	public void testPuzzleRoundtrip() throws Exception {
		long folderId = db.createFolder("folder");

		assertEquals(0, db.getNumberOfPuzzles(folderId));

		String name = "Hyper Squiggly";
		Difficulty difficulty = Difficulty.HARD;
		String clues = ".8....3.2....12..43.....4.5...1...................3...2.8.....31..59....4.6....5.";
		String areas = "111233334111233344112225334612225544662555744665577784699577788669997888699997888";
		String extra = "H";

		PuzzleInfo puzzleInfo = new PuzzleInfo.Builder(clues).setAreas(areas).setExtraRegions(extra)
				.setName(name).setDifficulty(difficulty).build();

		long puzzleId = db.insertPuzzle(folderId, puzzleInfo);
		assertTrue(puzzleId >= 0);

		assertEquals(1, db.getNumberOfPuzzles(folderId));

		PuzzleInfo loaded = db.loadPuzzle(folderId, 0);
		assertEquals(9, loaded.getSize());
		assertEquals(name, loaded.getName());
		assertEquals(difficulty, loaded.getDifficulty());
		assertEquals(clues, loaded.getClues());
		assertEquals(areas, loaded.getAreas());
		assertEquals(extra, loaded.getExtraRegions());
	}

	public void testInsertPuzzles() throws Exception {
		long folderId = db.createFolder("folder");

		String clues1 = ".8.4.96536428...7.......8....7..5.42...7.1...85.6..1....6.......1...47362735.8.1.";
		PuzzleInfo puzzle1 = new PuzzleInfo.Builder(clues1).build();
		String clues2 = "63.2.8.1.2...5..891.9.6..3...8..6.5....187....6.5..9...9..7.1.681..2...5.2.4.3.97";
		PuzzleInfo puzzle2 = new PuzzleInfo.Builder(clues2).build();
		String clues3 = "...1...4.195..8...34..2.1.9...91.5..6.98.24.7..1.34...2.8.4..71...7..832.1...9...";
		PuzzleInfo puzzle3 = new PuzzleInfo.Builder(clues3).build();
		String clues4 = "..7....638.467.9...1..39..2..37..6..7..4.1..5..8..61..6..21..9...1.635.839....7..";
		PuzzleInfo puzzle4 = new PuzzleInfo.Builder(clues4).build();
		String clues5 = "981...7..3.719..8...4.82...7..6..39.2.......7.13..7..5...35.2...6..148.3..2...564";
		PuzzleInfo puzzle5 = new PuzzleInfo.Builder(clues5).build();

		assertEquals(0, db.getNumberOfPuzzles(folderId));
		db.insertPuzzle(folderId, puzzle1);
		assertEquals(1, db.getNumberOfPuzzles(folderId));
		db.insertPuzzle(folderId, puzzle2);
		assertEquals(2, db.getNumberOfPuzzles(folderId));
		db.insertPuzzle(folderId, puzzle3);
		assertEquals(3, db.getNumberOfPuzzles(folderId));
		db.insertPuzzle(folderId, puzzle4);
		assertEquals(4, db.getNumberOfPuzzles(folderId));
		db.insertPuzzle(folderId, puzzle5);
		assertEquals(5, db.getNumberOfPuzzles(folderId));

		assertEquals(clues1, db.loadPuzzle(folderId, 0).getClues());
		assertEquals(clues2, db.loadPuzzle(folderId, 1).getClues());
		assertEquals(clues3, db.loadPuzzle(folderId, 2).getClues());
		assertEquals(clues4, db.loadPuzzle(folderId, 3).getClues());
		assertEquals(clues5, db.loadPuzzle(folderId, 4).getClues());
	}

	public void testGetPuzzleNumber() throws Exception {
		long folderId = db.createFolder("folder");

		String clues1 = ".8.4.96536428...7.......8....7..5.42...7.1...85.6..1....6.......1...47362735.8.1.";
		PuzzleInfo puzzle1 = new PuzzleInfo.Builder(clues1).build();
		String clues2 = "63.2.8.1.2...5..891.9.6..3...8..6.5....187....6.5..9...9..7.1.681..2...5.2.4.3.97";
		PuzzleInfo puzzle2 = new PuzzleInfo.Builder(clues2).build();
		String clues3 = "...1...4.195..8...34..2.1.9...91.5..6.98.24.7..1.34...2.8.4..71...7..832.1...9...";
		PuzzleInfo puzzle3 = new PuzzleInfo.Builder(clues3).build();
		String clues4 = "..7....638.467.9...1..39..2..37..6..7..4.1..5..8..61..6..21..9...1.635.839....7..";
		PuzzleInfo puzzle4 = new PuzzleInfo.Builder(clues4).build();
		String clues5 = "981...7..3.719..8...4.82...7..6..39.2.......7.13..7..5...35.2...6..148.3..2...564";
		PuzzleInfo puzzle5 = new PuzzleInfo.Builder(clues5).build();

		long id1 = db.insertPuzzle(folderId, puzzle1);
		long id2 = db.insertPuzzle(folderId, puzzle2);
		long id3 = db.insertPuzzle(folderId, puzzle3);
		long id4 = db.insertPuzzle(folderId, puzzle4);
		long id5 = db.insertPuzzle(folderId, puzzle5);

		assertEquals(0, db.getPuzzleNumber(folderId, id1));
		assertEquals(1, db.getPuzzleNumber(folderId, id2));
		assertEquals(2, db.getPuzzleNumber(folderId, id3));
		assertEquals(3, db.getPuzzleNumber(folderId, id4));
		assertEquals(4, db.getPuzzleNumber(folderId, id5));
	}

	public void testDeletePuzzle() throws Exception {
		long folderId = db.createFolder("folder");

		String clues = ".8.4.96536428...7.......8....7..5.42...7.1...85.6..1....6.......1...47362735.8.1.";
		PuzzleInfo puzzle = new PuzzleInfo.Builder(clues).build();

		long puzzleId = db.insertPuzzle(folderId, puzzle);
		assertEquals(1, db.getNumberOfPuzzles(folderId));

		db.deletePuzzle(puzzleId);
		assertEquals(0, db.getNumberOfPuzzles(folderId));
	}

	public void testPuzzlesBelongToFolder() throws Exception {
		long folderId1 = db.createFolder("folder1");
		long folderId2 = db.createFolder("folder2");

		String clues1 = ".8.4.96536428...7.......8....7..5.42...7.1...85.6..1....6.......1...47362735.8.1.";
		PuzzleInfo puzzle1 = new PuzzleInfo.Builder(clues1).build();
		String clues2 = "63.2.8.1.2...5..891.9.6..3...8..6.5....187....6.5..9...9..7.1.681..2...5.2.4.3.97";
		PuzzleInfo puzzle2 = new PuzzleInfo.Builder(clues2).build();
		String clues3 = "...1...4.195..8...34..2.1.9...91.5..6.98.24.7..1.34...2.8.4..71...7..832.1...9...";
		PuzzleInfo puzzle3 = new PuzzleInfo.Builder(clues3).build();
		String clues4 = "..7....638.467.9...1..39..2..37..6..7..4.1..5..8..61..6..21..9...1.635.839....7..";
		PuzzleInfo puzzle4 = new PuzzleInfo.Builder(clues4).build();
		String clues5 = "981...7..3.719..8...4.82...7..6..39.2.......7.13..7..5...35.2...6..148.3..2...564";
		PuzzleInfo puzzle5 = new PuzzleInfo.Builder(clues5).build();

		assertEquals(0, db.getNumberOfPuzzles(folderId1));
		assertEquals(0, db.getNumberOfPuzzles(folderId2));

		db.insertPuzzle(folderId1, puzzle1);
		assertEquals(1, db.getNumberOfPuzzles(folderId1));
		assertEquals(0, db.getNumberOfPuzzles(folderId2));

		db.insertPuzzle(folderId2, puzzle2);
		assertEquals(1, db.getNumberOfPuzzles(folderId1));
		assertEquals(1, db.getNumberOfPuzzles(folderId2));

		db.insertPuzzle(folderId1, puzzle3);
		assertEquals(2, db.getNumberOfPuzzles(folderId1));
		assertEquals(1, db.getNumberOfPuzzles(folderId2));

		db.insertPuzzle(folderId2, puzzle4);
		assertEquals(2, db.getNumberOfPuzzles(folderId1));
		assertEquals(2, db.getNumberOfPuzzles(folderId2));

		db.insertPuzzle(folderId1, puzzle5);
		assertEquals(3, db.getNumberOfPuzzles(folderId1));
		assertEquals(2, db.getNumberOfPuzzles(folderId2));

		assertEquals(clues1, db.loadPuzzle(folderId1, 0).getClues());
		assertEquals(clues2, db.loadPuzzle(folderId2, 0).getClues());
		assertEquals(clues3, db.loadPuzzle(folderId1, 1).getClues());
		assertEquals(clues4, db.loadPuzzle(folderId2, 1).getClues());
		assertEquals(clues5, db.loadPuzzle(folderId1, 2).getClues());
	}

	public void testDeleteFolderDeletesPuzzles() throws Exception {
		long folderId1 = db.createFolder("folder1");
		long folderId2 = db.createFolder("folder2");

		String clues1 = ".8.4.96536428...7.......8....7..5.42...7.1...85.6..1....6.......1...47362735.8.1.";
		PuzzleInfo puzzle1 = new PuzzleInfo.Builder(clues1).build();
		String clues2 = "63.2.8.1.2...5..891.9.6..3...8..6.5....187....6.5..9...9..7.1.681..2...5.2.4.3.97";
		PuzzleInfo puzzle2 = new PuzzleInfo.Builder(clues2).build();
		String clues3 = "...1...4.195..8...34..2.1.9...91.5..6.98.24.7..1.34...2.8.4..71...7..832.1...9...";
		PuzzleInfo puzzle3 = new PuzzleInfo.Builder(clues3).build();
		String clues4 = "..7....638.467.9...1..39..2..37..6..7..4.1..5..8..61..6..21..9...1.635.839....7..";
		PuzzleInfo puzzle4 = new PuzzleInfo.Builder(clues4).build();
		String clues5 = "981...7..3.719..8...4.82...7..6..39.2.......7.13..7..5...35.2...6..148.3..2...564";
		PuzzleInfo puzzle5 = new PuzzleInfo.Builder(clues5).build();

		db.insertPuzzle(folderId1, puzzle1);
		db.insertPuzzle(folderId2, puzzle2);
		db.insertPuzzle(folderId1, puzzle3);
		db.insertPuzzle(folderId2, puzzle4);
		db.insertPuzzle(folderId1, puzzle5);

		db.deleteFolder(folderId1);

		assertEquals(0, db.getNumberOfPuzzles(folderId1));
		assertEquals(2, db.getNumberOfPuzzles(folderId2));

		assertEquals(clues2, db.loadPuzzle(folderId2, 0).getClues());
		assertEquals(clues4, db.loadPuzzle(folderId2, 1).getClues());
	}
}
