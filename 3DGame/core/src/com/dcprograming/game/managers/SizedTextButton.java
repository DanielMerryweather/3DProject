/**
 * @author Colton Giesbrecht
 * @dateCreated Jun 4, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.managers;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SizedTextButton extends TextButton {

	private float prefWidth, prefHeight;

	/**
	 * @param text
	 * @param style
	 */
	public SizedTextButton(String text, TextButtonStyle style, float prefWidth, float prefHeight) {
		super(text, style);
		this.prefHeight = prefHeight;
		this.prefWidth = prefWidth;
	}

	public float getPrefWidth() {
		return prefWidth;
	}

	public float getPrefHeight() {
		return prefHeight;
	}
}
