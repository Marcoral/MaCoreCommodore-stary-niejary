package pl.mateam.marpg.engine.core.objects.users.children.bans;

import java.util.Date;

import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBansBuilder;

public class BansBuilderInstance implements CommodoreBansBuilder {
	private BanObjectInstance banObject;
	private BanBlendMode blendMode = BanBlendMode.OVERWRITE_EXISTING;
	
	public BansBuilderInstance(BanObjectInstance banObject) {
		this.banObject = banObject;
	}
	
	@Override
	public CommodoreBansBuilder setWhoBanned(String nickname) {
		banObject.setWhoBanned(nickname);
		return this;
	}

	@Override
	public CommodoreBansBuilder setExpirationDate(Date date, BanBlendMode blendMode) {
		this.blendMode = blendMode;
		banObject.setExpirationDate(date);
		return this;
	}
	
	public BanBlendMode getBlendMode() {
		return blendMode;
	}
}
