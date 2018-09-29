package pl.mateam.marpg.api.objects.users;

public interface CommodoreGamemaster extends CommodoreUser {
	boolean isVisible();
	void setVisible(boolean isVisible);
}
