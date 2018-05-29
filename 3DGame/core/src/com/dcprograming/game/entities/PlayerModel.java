package com.dcprograming.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class PlayerModel {
	
	public Entity model;
	
	public PlayerModel(Color team, ModelBuilder mb) {
		model = new Entity(mb.createBox(1, 1, 1, new Material(ColorAttribute.createDiffuse(team)), Usage.Normal | Usage.Position), 0,0,0);
	}
	
	public void rotate(float pitch, float yaw) {
		//model.transform.rotate(axis, degrees);
	}

}
