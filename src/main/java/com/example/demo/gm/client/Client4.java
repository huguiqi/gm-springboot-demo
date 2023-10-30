package com.example.demo.gm.client;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

/**
 * 使用HttpsURLConnection（双向）
 * @author gmssl.cn
 */
public class Client4
{
	public Client4()
	{}

	public static void main(String[] args)
	{
		SSLSocketFactory fact = null;
		HttpsURLConnection conn = null;

		System.out.println("Usage: java -cp GMExample.jar client.Client4 url");
		
		try
		{
//			String urlStr = "https://demo.gmssl.cn:1443/";
			String urlStr = "https://192.168.1.116/";

			Security.insertProviderAt((Provider)Class.forName("cn.gmssl.jce.provider.GMJCE").newInstance(), 1);
			Security.insertProviderAt((Provider)Class.forName("cn.gmssl.jsse.provider.GMJSSE").newInstance(), 2);
			
			String pfxfile = "keystore/sm2.user.both.pfx";
			String pwd = "12345678";
			ClassLoader classLoader = Client4.class.getClassLoader();
			File configFile=new File(classLoader.getResource(pfxfile).getFile());
        	KeyStore pfx = KeyStore.getInstance("PKCS12","GMJCE");
        	pfx.load(new FileInputStream(configFile), pwd.toCharArray());
 			fact = createSocketFactory(pfx, pwd.toCharArray());
			
			SSLSocketFactory fact2 = new PreferredCipherSuiteSSLSocketFactory(fact);
			
			URL url = new URL(urlStr);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setInstanceFollowRedirects(true);
			conn.setSSLSocketFactory(fact2);
			conn.setHostnameVerifier(new HostnameVerifier() 
			{
				public boolean verify(String hostname, SSLSession session) 
				{
					return true;
				}
			});
			
			conn.connect();
			
			InputStream input = conn.getInputStream();
			byte[] buffer = new byte[1024 * 4];
			int length = 0;
			while ((length = input.read(buffer)) != -1) 
			{
				System.out.println(new String(buffer, 0, length));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				conn.disconnect();
			}
			catch (Exception e)
			{}
		}
	}

	public static SSLSocketFactory createSocketFactory(KeyStore kepair, char[] pwd) throws Exception
	{
		TrustAllManager[] trust = { new TrustAllManager() };

		KeyManager[] kms = null;
		if (kepair != null)
		{
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(kepair, pwd);
			kms = kmf.getKeyManagers();
		}

		SSLContext ctx = SSLContext.getInstance("GMSSLv1.1", "GMJSSE");
		SecureRandom secureRandom = new SecureRandom();
		ctx.init(kms, trust, secureRandom);

		ctx.getServerSessionContext().setSessionCacheSize(8192);
		ctx.getServerSessionContext().setSessionTimeout(3600);

		SSLSocketFactory factory = ctx.getSocketFactory();
		return factory;
	}
}

