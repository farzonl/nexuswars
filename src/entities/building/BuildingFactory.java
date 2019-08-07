package entities.building;

import entities.unit.UnitFactory;
import game.engine.GameConstants;
import game.engine.Player;
import game.graphics.Sprite;
import geom.Dimension;
import geom.Point;

public class BuildingFactory {

	public static Building barracks(Player p, int x, int y) {
		return BuildingImpl
				.builder()
				.owner(p)
				.location(new Point(x, y))
				.setBuildingType(GameConstants.Barrack_BLD)
				.cost(5)
				.buildTime(10000)
				.maxHP(1000)
				.unitProducer(UnitFactory.marineProducer(p))
				.sprite(new Sprite(new Dimension(32, 32),
						"../res/buildings/p0b1.png")).build();
	}

	public static Building dojo(Player p, int x, int y) {
		return BuildingImpl
				.builder()
				.owner(p)
				.location(new Point(x, y))
				.buildTime(13000)
				.maxHP(1000)
				.setBuildingType(GameConstants.DOJO_BLD)
				.unitProducer(UnitFactory.meleeProducer(p))
				.cost(8)
				.sprite(new Sprite(new Dimension(32, 32),
						"../res/buildings/p0b2.png")).build();

	}

	public static Building academy(Player p, int x, int y) {
		return BuildingImpl
				.builder()
				.owner(p)
				.location(new Point(x, y))
				.buildTime(11000)
				.addIncome(2)
				.cost(10)
				.maxHP(1500)
				.setBuildingType(GameConstants.ACAD_BLD)
				.unitProducer(UnitFactory.sniperProducer(p))
				.sprite(new Sprite(new Dimension(32, 32),
						"../res/buildings/p0b3.png")).build();
	}

	public static Building rpgfactroy(Player p, int x, int y) {
		return BuildingImpl
				.builder()
				.owner(p)
				.location(new Point(x, y))
				.buildTime(14000)
				.setBuildingType(GameConstants.RPG_BLD)
				.cost(15)
				.maxHP(1500)
				.unitProducer(UnitFactory.rocketProducer(p))
				.sprite(new Sprite(new Dimension(32, 32),
						"../res/buildings/p0b4.png")).build();
	}

	public static Building tankfactory(Player p, int x, int y) {
		return BuildingImpl
				.builder()
				.owner(p)
				.location(new Point(x, y))
				.buildTime(15000)
				.maxHP(1500)
				.setBuildingType(GameConstants.SHIELD_BLD)
				.cost(15)
				.unitProducer(UnitFactory.tankProducer(p))
				.sprite(new Sprite(new Dimension(32, 32),
						"../res/buildings/p0b5.png")).build();
	}

	public static Building nexus(Player p, int x, int y) {
		return BuildingImpl
				.builder()
				.owner(p)
				.location(new Point(x, y))
				.buildTime(10000)
				.maxHP(10000)
				.addIncome(1)
				.setBuildingType(GameConstants.NEXUS_BLD)
				.unitProducer(null)
				.cost(0)
				.sprite(new Sprite(new Dimension(32, 32),
						"../res/buildings/p0b0.png")).build();
	}
}
