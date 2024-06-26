/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.bleachhack.setting.module;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.bleachhack.setting.SettingDataHandlers;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class SettingBlockList extends SettingList<Block> {

	public SettingBlockList(String text, String windowText, Block... defaultBlocks) {
		this(text, windowText, null, defaultBlocks);
	}

	public SettingBlockList(String text, String windowText, Predicate<Block> filter, Block... defaultBlocks) {
		super(text, windowText, SettingDataHandlers.BLOCK, getAllBlocks(filter), defaultBlocks);
	}

	private static Collection<Block> getAllBlocks(Predicate<Block> filter) {
		return filter == null
				? Registries.BLOCK.stream().collect(Collectors.toList())
						: Registries.BLOCK.stream().filter(filter).collect(Collectors.toList());
	}

	@Override
	public void renderItem(MinecraftClient mc, DrawContext drawContext, Block item, int x, int y, int w, int h) {
		if (item == null || item.asItem() == Items.AIR) {
			super.renderItem(mc, drawContext, item, x, y, w, h);
		} else {
			RenderSystem.getModelViewStack().push();

			float scale = (h - 2) / 16f;
			float offset = 1f / scale;

			RenderSystem.getModelViewStack().scale(scale, scale, 1f);

			drawContext.drawItem(new ItemStack(item.asItem()), (int) ((x + 1) * offset), (int) ((y + 1) * offset));

			RenderSystem.getModelViewStack().pop();
			RenderSystem.applyModelViewMatrix();
		}
	}

	@Override
	public Text getName(Block item) {
		return item.getName();
	}
}
