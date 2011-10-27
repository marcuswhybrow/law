package net.marcuswhybrow.minecraft.law.interfaces;

public interface Saveable {
	
	public void setup();
	public void save(boolean forceFullSave);
	public void delete();
	
	public void onSetup();
	
	public void setChanged(String sectionName, boolean state);
	public boolean isChanged(String sectionName);
	
	public void setConfigPrefix(String configPrefix);
	public String getConfigPrefix();
	
	public void configSet(String relativePath, Object o);
	public void configSave();
	
	public boolean canDelete();
}
