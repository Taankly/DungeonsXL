/*
 * Copyright (C) 2012-2017 Frank Baumann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.dre2n.dungeonsxl.world.block;

import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.dungeonsxl.config.DMessages;
import io.github.dre2n.dungeonsxl.player.DGamePlayer;
import io.github.dre2n.dungeonsxl.player.DGroup;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Bed;

/**
 * @author Daniel Saukel
 */
public class TeamBed extends TeamBlock implements MultiBlock {

    private Block attachedBlock;

    public TeamBed(Block block, DGroup owner) {
        super(block, owner);
        attachedBlock = getAttachedBlock();
    }

    /* Getters and setters */
    public Block getAttachedBlock(Block block) {
        if (block.getRelative(BlockFace.EAST).getType() == Material.BED_BLOCK) {
            return block.getRelative(BlockFace.EAST);

        } else if (block.getRelative(BlockFace.NORTH).getType() == Material.BED_BLOCK) {
            return block.getRelative(BlockFace.NORTH);

        } else if (block.getRelative(BlockFace.WEST).getType() == Material.BED_BLOCK) {
            return block.getRelative(BlockFace.WEST);

        } else if (block.getRelative(BlockFace.SOUTH).getType() == Material.BED_BLOCK) {
            return block.getRelative(BlockFace.SOUTH);

        } else {
            return null;
        }
    }

    @Override
    public Block getAttachedBlock() {
        if (attachedBlock != null) {
            return attachedBlock;

        } else {
            return getAttachedBlock(block);
        }
    }

    /* Actions */
    @Override
    public boolean onBreak(BlockBreakEvent event) {
        Player breaker = event.getPlayer();
        if (owner.getPlayers().contains(breaker)) {
            MessageUtil.sendMessage(breaker, DMessages.ERROR_BLOCK_OWN_TEAM.getMessage());
            return true;
        }

        for (DGamePlayer player : owner.getDGamePlayers()) {
            player.setLives(1);
        }
        owner.setLives(0);

        owner.getGameWorld().sendMessage(DMessages.GROUP_BED_DESTROYED.getMessage(owner.getName(), DGamePlayer.getByPlayer(breaker).getName()));
        Block block1 = event.getBlock();
        if (((Bed) block1.getState().getData()).isHeadOfBed()) {
            Block block2 = getAttachedBlock(block1);
            if (block2 != null) {
                block2.setType(Material.AIR);
            }
        }
        block1.setType(Material.AIR);
        return true;
    }

}
