package de.basic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import de.basic.main.Main;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HDFSConnection {
	Properties prop = new Properties();
	private FileSystem fs;
	private Configuration conf;
	
	public HDFSConnection() {
		createConnection();
	}
	
	private void createConnection() {
		conf = new Configuration();
		
		conf.set("fs.default.name", "hdfs://master:9000");
		fs = null;
		
		try {
			prop.load(new FileInputStream("hdfs.properties"));
			
			conf.addResource(new Path(prop.getProperty("hdfs_core_site") + "core-site.xml"));
			conf.addResource(new Path(prop.getProperty("hdfs_core_site") + "hdfs-site.xml"));
			
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
