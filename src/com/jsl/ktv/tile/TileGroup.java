package com.jsl.ktv.tile;

/**
 * 单个页面的一些属�? */
import java.util.ArrayList;

public class TileGroup {
	private ArrayList<Tile> tiles;
	private int maxTiles = 9;
	private String title;

	public TileGroup() {
		tiles = new ArrayList<Tile>(maxTiles);
	}

	public void addTile(Tile tile) {
		// TODO Auto-generated method stub
		tiles.add(tile);
	}

	public Tile getTileAt(int i) {
		// TODO Auto-generated method stub
		return tiles.get(i);
	}

	/**
	 * 返回每个页面的图片�?数的方法
	 *
	 * @return 每个页面的图片�?�?	 */
	public int getTileCount() {
		return tiles.size();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}
}
