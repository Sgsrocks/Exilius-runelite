package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.RSTile;
import net.runelite.rs.api.RSTileItem;

import java.util.ArrayList;
import java.util.List;

public final class Ground extends Linkable implements RSTile {

	public Ground(int i, int j, int k) {
		obj5Array = new StaticObject[5];
		anIntArray1319 = new int[5];
		anInt1310 = anInt1307 = i;
		anInt1308 = j;
		anInt1309 = k;
	}

	int anInt1307;
	final int anInt1308;
	final int anInt1309;
	final int anInt1310;
	public Class43 aClass43_1311;
	public ShapedTile aClass40_1312;
	public Object1 obj1;
	public Object2 obj2;
	public Object3 obj3;
	public Object4 obj4;
	int anInt1317;
	public final StaticObject[] obj5Array;
	final int[] anIntArray1319;
	int anInt1320;
	int anInt1321;
	boolean aBoolean1322;
	boolean aBoolean1323;
	boolean aBoolean1324;
	int anInt1325;
	int anInt1326;
	int anInt1327;
	int anInt1328;
	public Ground aClass30_Sub3_1329;

	@Override
	public WorldPoint getWorldLocation() {
		return null;
	}

	@Override
	public LocalPoint getLocalLocation() {
		return null;
	}

	@Override
	public Point getSceneLocation() {
		return new Point(this.anInt1308, this.anInt1309);
	}

	@Override
	public boolean hasLineOfSightTo(net.runelite.api.Tile other) {
		// Thanks to Henke for this method :)

		if (this.getPlane() != other.getPlane())
		{
			return false;
		}

		CollisionData[] collisionData = Client.instance.getCollisionMaps();
		if (collisionData == null)
		{
			return false;
		}

		int z = this.getPlane();
		int[][] collisionDataFlags = collisionData[z].getFlags();

		Point p1 = this.getSceneLocation();
		Point p2 = other.getSceneLocation();
		if (p1.getX() == p2.getX() && p1.getY() == p2.getY())
		{
			return true;
		}

		int dx = p2.getX() - p1.getX();
		int dy = p2.getY() - p1.getY();
		int dxAbs = Math.abs(dx);
		int dyAbs = Math.abs(dy);

		int xFlags = CollisionDataFlag.BLOCK_LINE_OF_SIGHT_FULL;
		int yFlags = CollisionDataFlag.BLOCK_LINE_OF_SIGHT_FULL;
		if (dx < 0)
		{
			xFlags |= CollisionDataFlag.BLOCK_LINE_OF_SIGHT_EAST;
		}
		else
		{
			xFlags |= CollisionDataFlag.BLOCK_LINE_OF_SIGHT_WEST;
		}
		if (dy < 0)
		{
			yFlags |= CollisionDataFlag.BLOCK_LINE_OF_SIGHT_NORTH;
		}
		else
		{
			yFlags |= CollisionDataFlag.BLOCK_LINE_OF_SIGHT_SOUTH;
		}

		if (dxAbs > dyAbs)
		{
			int x = p1.getX();
			int yBig = p1.getY() << 16; // The y position is represented as a bigger number to handle rounding
			int slope = (dy << 16) / dxAbs;
			yBig += 0x8000; // Add half of a tile
			if (dy < 0)
			{
				yBig--; // For correct rounding
			}
			int direction = dx < 0 ? -1 : 1;

			while (x != p2.getX())
			{
				x += direction;
				int y = yBig >>> 16;
				if ((collisionDataFlags[x][y] & xFlags) != 0)
				{
					// Collision while traveling on the x axis
					return false;
				}
				yBig += slope;
				int nextY = yBig >>> 16;
				if (nextY != y && (collisionDataFlags[x][nextY] & yFlags) != 0)
				{
					// Collision while traveling on the y axis
					return false;
				}
			}
		}
		else
		{
			int y = p1.getY();
			int xBig = p1.getX() << 16; // The x position is represented as a bigger number to handle rounding
			int slope = (dx << 16) / dyAbs;
			xBig += 0x8000; // Add half of a tile
			if (dx < 0)
			{
				xBig--; // For correct rounding
			}
			int direction = dy < 0 ? -1 : 1;

			while (y != p2.getY())
			{
				y += direction;
				int x = xBig >>> 16;
				if ((collisionDataFlags[x][y] & yFlags) != 0)
				{
					// Collision while traveling on the y axis
					return false;
				}
				xBig += slope;
				int nextX = xBig >>> 16;
				if (nextX != x && (collisionDataFlags[nextX][y] & xFlags) != 0)
				{
					// Collision while traveling on the x axis
					return false;
				}
			}
		}

		// No collision
		return true;
	}


	@Override
	public List<net.runelite.api.Tile> pathTo(net.runelite.api.Tile other) {
		int z = this.getPlane();
		if (z != other.getPlane())
		{
			return null;
		}

		CollisionData[] collisionData = Client.instance.getCollisionMaps();
		if (collisionData == null)
		{
			return null;
		}

		int[][] directions = new int[128][128];
		int[][] distances = new int[128][128];
		int[] bufferX = new int[4096];
		int[] bufferY = new int[4096];

		// Initialise directions and distances
		for (int i = 0; i < 128; ++i)
		{
			for (int j = 0; j < 128; ++j)
			{
				directions[i][j] = 0;
				distances[i][j] = Integer.MAX_VALUE;
			}
		}

		Point p1 = this.getSceneLocation();
		Point p2 = other.getSceneLocation();

		int middleX = p1.getX();
		int middleY = p1.getY();
		int currentX = middleX;
		int currentY = middleY;
		int offsetX = 64;
		int offsetY = 64;
		// Initialise directions and distances for starting tile
		directions[offsetX][offsetY] = 99;
		distances[offsetX][offsetY] = 0;
		int index1 = 0;
		bufferX[0] = currentX;
		int index2 = 1;
		bufferY[0] = currentY;
		int[][] collisionDataFlags = collisionData[z].getFlags();

		boolean isReachable = false;

		while (index1 != index2)
		{
			currentX = bufferX[index1];
			currentY = bufferY[index1];
			index1 = index1 + 1 & 4095;
			// currentX is for the local coordinate while currentMapX is for the index in the directions and distances arrays
			int currentMapX = currentX - middleX + offsetX;
			int currentMapY = currentY - middleY + offsetY;
			if ((currentX == p2.getX()) && (currentY == p2.getY()))
			{
				isReachable = true;
				break;
			}

			int currentDistance = distances[currentMapX][currentMapY] + 1;
			if (currentMapX > 0 && directions[currentMapX - 1][currentMapY] == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0)
			{
				// Able to move 1 tile west
				bufferX[index2] = currentX - 1;
				bufferY[index2] = currentY;
				index2 = index2 + 1 & 4095;
				directions[currentMapX - 1][currentMapY] = 2;
				distances[currentMapX - 1][currentMapY] = currentDistance;
			}

			if (currentMapX < 127 && directions[currentMapX + 1][currentMapY] == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0)
			{
				// Able to move 1 tile east
				bufferX[index2] = currentX + 1;
				bufferY[index2] = currentY;
				index2 = index2 + 1 & 4095;
				directions[currentMapX + 1][currentMapY] = 8;
				distances[currentMapX + 1][currentMapY] = currentDistance;
			}

			if (currentMapY > 0 && directions[currentMapX][currentMapY - 1] == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
			{
				// Able to move 1 tile south
				bufferX[index2] = currentX;
				bufferY[index2] = currentY - 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX][currentMapY - 1] = 1;
				distances[currentMapX][currentMapY - 1] = currentDistance;
			}

			if (currentMapY < 127 && directions[currentMapX][currentMapY + 1] == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
			{
				// Able to move 1 tile north
				bufferX[index2] = currentX;
				bufferY[index2] = currentY + 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX][currentMapY + 1] = 4;
				distances[currentMapX][currentMapY + 1] = currentDistance;
			}

			if (currentMapX > 0 && currentMapY > 0 && directions[currentMapX - 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX - 1][currentY - 1] & 19136782) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
			{
				// Able to move 1 tile south-west
				bufferX[index2] = currentX - 1;
				bufferY[index2] = currentY - 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX - 1][currentMapY - 1] = 3;
				distances[currentMapX - 1][currentMapY - 1] = currentDistance;
			}

			if (currentMapX < 127 && currentMapY > 0 && directions[currentMapX + 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX + 1][currentY - 1] & 19136899) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
			{
				// Able to move 1 tile north-west
				bufferX[index2] = currentX + 1;
				bufferY[index2] = currentY - 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX + 1][currentMapY - 1] = 9;
				distances[currentMapX + 1][currentMapY - 1] = currentDistance;
			}

			if (currentMapX > 0 && currentMapY < 127 && directions[currentMapX - 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX - 1][currentY + 1] & 19136824) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
			{
				// Able to move 1 tile south-east
				bufferX[index2] = currentX - 1;
				bufferY[index2] = currentY + 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX - 1][currentMapY + 1] = 6;
				distances[currentMapX - 1][currentMapY + 1] = currentDistance;
			}

			if (currentMapX < 127 && currentMapY < 127 && directions[currentMapX + 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX + 1][currentY + 1] & 19136992) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
			{
				// Able to move 1 tile north-east
				bufferX[index2] = currentX + 1;
				bufferY[index2] = currentY + 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX + 1][currentMapY + 1] = 12;
				distances[currentMapX + 1][currentMapY + 1] = currentDistance;
			}
		}
		if (!isReachable)
		{
			// Try find a different reachable tile in the 21x21 area around the target tile, as close as possible to the target tile
			int upperboundDistance = Integer.MAX_VALUE;
			int pathLength = Integer.MAX_VALUE;
			int checkRange = 10;
			int approxDestinationX = p2.getX();
			int approxDestinationY = p2.getY();
			for (int i = approxDestinationX - checkRange; i <= checkRange + approxDestinationX; ++i)
			{
				for (int j = approxDestinationY - checkRange; j <= checkRange + approxDestinationY; ++j)
				{
					int currentMapX = i - middleX + offsetX;
					int currentMapY = j - middleY + offsetY;
					if (currentMapX >= 0 && currentMapY >= 0 && currentMapX < 128 && currentMapY < 128 && distances[currentMapX][currentMapY] < 100)
					{
						int deltaX = 0;
						if (i < approxDestinationX)
						{
							deltaX = approxDestinationX - i;
						}
						else if (i > approxDestinationX)
						{
							deltaX = i - (approxDestinationX);
						}

						int deltaY = 0;
						if (j < approxDestinationY)
						{
							deltaY = approxDestinationY - j;
						}
						else if (j > approxDestinationY)
						{
							deltaY = j - (approxDestinationY);
						}

						int distanceSquared = deltaX * deltaX + deltaY * deltaY;
						if (distanceSquared < upperboundDistance || distanceSquared == upperboundDistance && distances[currentMapX][currentMapY] < pathLength)
						{
							upperboundDistance = distanceSquared;
							pathLength = distances[currentMapX][currentMapY];
							currentX = i;
							currentY = j;
						}
					}
				}
			}
			if (upperboundDistance == Integer.MAX_VALUE)
			{
				// No path found
				return null;
			}
		}

		// Getting path from directions and distances
		bufferX[0] = currentX;
		bufferY[0] = currentY;
		int index = 1;
		int directionNew;
		int directionOld;
		for (directionNew = directionOld = directions[currentX - middleX + offsetX][currentY - middleY + offsetY]; p1.getX() != currentX || p1.getY() != currentY; directionNew = directions[currentX - middleX + offsetX][currentY - middleY + offsetY])
		{
			if (directionNew != directionOld)
			{
				// "Corner" of the path --> new checkpoint tile
				directionOld = directionNew;
				bufferX[index] = currentX;
				bufferY[index++] = currentY;
			}

			if ((directionNew & 2) != 0)
			{
				++currentX;
			}
			else if ((directionNew & 8) != 0)
			{
				--currentX;
			}

			if ((directionNew & 1) != 0)
			{
				++currentY;
			}
			else if ((directionNew & 4) != 0)
			{
				--currentY;
			}
		}

		int checkpointTileNumber = 1;
		Tile[][][] tiles = (Tile[][][]) Client.instance.getScene().getTiles();
		List<net.runelite.api.Tile> checkpointTiles = new ArrayList<>();
		while (index-- > 0)
		{
			checkpointTiles.add(tiles[this.getPlane()][bufferX[index]][bufferY[index]]);
			if (checkpointTileNumber == 25)
			{
				// Pathfinding only supports up to the 25 first checkpoint tiles
				break;
			}
			checkpointTileNumber++;
		}
		return checkpointTiles;
	}


	@Override
	public List<TileItem> getGroundItems() {
		ItemLayer layer = this.getItemLayer();
		if (layer == null)
		{
			return null;
		}

		List<TileItem> result = new ArrayList<TileItem>();
		Node node = layer.getBottom();
		while (node instanceof RSTileItem)
		{
			RSTileItem item = (RSTileItem) node;
			item.setX(getX());
			item.setY(getY());
			result.add(item);
			node = node.getNext();
		}
		return result;
	}


	@Override
	public net.runelite.api.GameObject[] getGameObjects() {
		return obj5Array;
	}
	@Override
	public ItemLayer getItemLayer() {
		return null;
	}

	@Override
	public DecorativeObject getDecorativeObject() {
		return null;
	}

	@Override
	public void setDecorativeObject(DecorativeObject object) {

	}

	@Override
	public GroundObject getGroundObject() {
		return null;
	}

	@Override
	public void setGroundObject(GroundObject object) {

	}

	@Override
	public WallObject getWallObject() {
		return null;
	}

	@Override
	public void setWallObject(WallObject object) {

	}

	@Override
	public SceneTilePaint getSceneTilePaint() {
		return null;
	}

	@Override
	public void setSceneTilePaint(SceneTilePaint paint) {

	}

	@Override
	public SceneTileModel getSceneTileModel() {
		return null;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getPlane() {
		return 0;
	}

	@Override
	public int getRenderLevel() {
		return 0;
	}

	@Override
	public int getPhysicalLevel() {
		return 0;
	}

	@Override
	public int getFlags() {
		return 0;
	}

	@Override
	public RSTile getBridge() {
		return null;
	}

	@Override
	public boolean isDraw() {
		return false;
	}

	@Override
	public void setDraw(boolean draw) {

	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void setVisible(boolean visible) {

	}

	@Override
	public void setDrawEntities(boolean drawEntities) {

	}

	@Override
	public void setWallCullDirection(int wallCullDirection) {

	}
}
