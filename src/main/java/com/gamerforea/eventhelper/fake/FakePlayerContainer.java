package com.gamerforea.eventhelper.fake;

import java.util.UUID;

import com.gamerforea.eventhelper.util.FastUtils;
import com.mojang.authlib.GameProfile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public abstract class FakePlayerContainer
{
	private final FakePlayer modFake;
	private FakePlayer player;
	public GameProfile profile;

	protected FakePlayerContainer(FakePlayer modFake)
	{
		this.modFake = modFake;
	}

	public abstract World getWorld();

	public final FakePlayer getPlayer()
	{
		if (this.player != null)
			return FastUtils.getFake(this.getWorld(), this.player);
		else if (this.profile != null)
			return this.player = FastUtils.getFake(this.getWorld(), this.profile);
		else
			return FastUtils.getFake(this.getWorld(), this.modFake);
	}

	public final void writeToNBT(NBTTagCompound nbt)
	{
		if (this.profile != null)
		{
			nbt.setString("eventhelper_fakeName", this.profile.getName());
			nbt.setString("eventhelper_fakeUUID", this.profile.getId().toString());
		}
	}

	public final void readFromNBT(NBTTagCompound nbt)
	{
		this.profile = readProfile(nbt, "eventhelper_fakeName", "eventhelper_fakeUUID");
		if (this.profile == null)
			this.profile = readProfile(nbt, "ownerName", "ownerUUID");
	}

	private static final GameProfile readProfile(NBTTagCompound nbt, String nameKey, String uuidKey)
	{
		String name = nbt.getString(nameKey);
		if (!name.isEmpty())
		{
			String uuid = nbt.getString(uuidKey);
			if (!uuid.isEmpty())
				return new GameProfile(UUID.fromString(uuid), name);
		}

		return null;
	}
}