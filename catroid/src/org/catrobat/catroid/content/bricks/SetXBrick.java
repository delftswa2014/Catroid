/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class SetXBrick extends BrickBaseType implements OnClickListener, FormulaBrick {
	private static final long serialVersionUID = 1L;
	private Formula xPosition;

	private transient View prototypeView;

	public SetXBrick(Sprite sprite, int xPositionValue) {
		this.sprite = sprite;
		xPosition = new Formula(xPositionValue);
	}

	public SetXBrick(Sprite sprite, Formula xPosition) {
		this.sprite = sprite;
		this.xPosition = xPosition;
	}

	public SetXBrick() {

	}

	@Override
	public Formula getFormula() {
		return xPosition;
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite, Script script) {
		SetXBrick copyBrick = (SetXBrick) clone();
		copyBrick.sprite = sprite;
		return copyBrick;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, R.layout.brick_set_x, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(R.id.brick_set_x_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});
		TextView textX = (TextView) view.findViewById(R.id.brick_set_x_prototype_text_view);
		TextView editX = (TextView) view.findViewById(R.id.brick_set_x_edit_text);

		xPosition.setTextFieldId(R.id.brick_set_x_edit_text);
		xPosition.refreshTextField(view);

		textX.setVisibility(View.GONE);
		editX.setVisibility(View.VISIBLE);
		editX.setOnClickListener(this);

		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(R.id.brick_set_x_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textX = (TextView) view.findViewById(R.id.brick_set_x_text_view);
			TextView editX = (TextView) view.findViewById(R.id.brick_set_x_edit_text);
			textX.setTextColor(textX.getTextColors().withAlpha(alphaValue));
			editX.setTextColor(editX.getTextColors().withAlpha(alphaValue));
			editX.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, R.layout.brick_set_x, null);
		TextView textXPosition = (TextView) prototypeView.findViewById(R.id.brick_set_x_prototype_text_view);
		textXPosition.setText(String.valueOf(xPosition.interpretInteger(sprite)));
		return prototypeView;
	}

	@Override
	public Brick clone() {
		return new SetXBrick(getSprite(), xPosition.clone());
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, xPosition);
	}

	@Override
	public List<SequenceAction> addActionToSequence(SequenceAction sequence) {
		sequence.addAction(ExtendedActions.setX(sprite, xPosition));
		return null;
	}
}
