package tech.mcprison.prison.troubleshoot.zip;

public class SupportZipEntry {
    // the file path in the zip file relative to the folder for this provider
    String path;
    byte[] data;

    /**
     * Gets the full path including file name in this provider's folder
     * @return the full path including file name
     */
    public String getPath(){
        return path;
    }

    /**
     * Sets the full path including file name in this provider's folder
     */
    public void setPath(String location){
        this.path = location;
    }
    public byte[] getData(){
        return data;
    }
    public void setData(byte[] data){
        this.data = data;
    }
}
