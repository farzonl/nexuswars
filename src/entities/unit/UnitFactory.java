package entities.unit;

import entities.Producer;
import game.engine.Player;
import game.graphics.Sprite;
import geom.Dimension;

public class UnitFactory {

	private static final Dimension d16 = new Dimension(16, 16);
	private static final Dimension d24 = new Dimension(24, 24);
	public static Unit marine(Player player) {
		return UnitImpl.builder().description("Marine", "Basic ranged unit")
				.multiplier(Armor.LIGHT, 0.9).multiplier(Armor.HEAVY, 0.5)
				.attackPower(10).armor(Armor.LIGHT).attackRange(50)
				.attackSpeed(450).maxHP(50).owner(player).moveSpeed(1)
				.sprite(new Sprite(d24, "../res/units/unit_01.png")).build();
	}

	public static Producer<Unit> marineProducer(final Player player) {
		return new Producer<Unit>() {
			@Override
			public Unit produce() {
				return marine(player);
			}
		};
	}

	public static Unit melee(Player player) {
		return UnitImpl.builder().description("Melee", "Basic melee unit")
				.armor(Armor.HEAVY).attackSpeed(500)
				.multiplier(Armor.LIGHT, 0.9).attackPower(25)
				.multiplier(Armor.HEAVY, 0.5).maxHP(55).owner(player)
				.attackRange(40)
				.sprite(new Sprite(d24, "../res/units/unit_04.png")).build();
	}

	public static Producer<Unit> meleeProducer(final Player player) {
		return new Producer<Unit>() {
			@Override
			public Unit produce() {
				return melee(player);
			}
		};
	}

	public static Unit sniper(Player player) {
		return UnitImpl.builder().description("Sniper", "Basic ranged unit")
				.multiplier(Armor.LIGHT, 0.9).attackSpeed(3000)
				.multiplier(Armor.HEAVY, 1.2).attackRange(120).maxHP(30)
				.owner(player).attackPower(50)
				.sprite(new Sprite(d24, "../res/units/unit_07.png")).build();
	}

	public static Producer<Unit> sniperProducer(final Player player) {
		return new Producer<Unit>() {
			@Override
			public Unit produce() {
				return sniper(player);
			}
		};
	}

	public static Unit rocket(Player player) {
		return UnitImpl.builder().description("Grenadier", "Basic ranged unit")
				.attackRange(150).attackRadius(10).attackSpeed(3000)
				.multiplier(Armor.LIGHT, 0.9).attackPower(350)
				.multiplier(Armor.HEAVY, 0.5).maxHP(175).owner(player)
				.sprite(new Sprite(d24, "../res/units/unit_50.png")).build();
	}

	public static Producer<Unit> rocketProducer(final Player player) {
		return new Producer<Unit>() {
			@Override
			public Unit produce() {
				return rocket(player);
			}
		};
	}

	public static Unit tank(Player player) {
		return UnitImpl
				.builder()
				.description("Human Shield",
						"Melee unit with low damage but high health and heavy armor")
				.armor(Armor.HEAVY).maxHP(150).attackSpeed(1500)
				.attackRange(80).multiplier(Armor.LIGHT, 0.9).attackPower(65)
				.multiplier(Armor.HEAVY, 0.5).owner(player)
				.sprite(new Sprite(d24, "../res/units/unit_60.png")).build();
	}

	public static Producer<Unit> tankProducer(final Player player) {
		return new Producer<Unit>() {
			@Override
			public Unit produce() {
				return tank(player);
			}
		};
	}
}
