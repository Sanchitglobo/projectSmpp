package com.globocom.matrix.mail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class churn {


	public static String ip="10.1.124.202";

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							"support@globocom.in", "Start100%");
				}
			});
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("support@globocom.in"));
			// message.setRecipients(Message.RecipientType.TO,
			// InternetAddress.parse("info@globocom.in"));
			//			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse("rk@globocom.in"));
			//			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse("saji@globocom.in"));
			//			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse("info@globocom.in"));
			//			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("info@globocom.in"));
			//				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("ak@globocom.in,nitish@globocom.in,gaurav.vaidya@globocom.in,s.kumar@globocom.in,info@globocom.in,saji@globocom.in,rk@globocom.in,support@globocom.in"));
			//				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("info@globocom.in,kumari@globocom.in"));
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse("kumari@globocom.in,support@globocom.in,info@globocom.in,rk@globocom.in,s.kumar@globocom.in,ak@globocom.in,saji@globocom.in"));
			//			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("s.kumar@globocom.in"));
			//			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("s.kumar@globocom.in"));
			Content cot = new Content();
			message.setSubject("Churn REPORT V3.0" + getCurrentTimeStampSubject());
			message.setContent(Header().toString(),"text/html");
			System.out.println("DONE");
			Transport.send(message);
			System.out.println("finally sent ");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}


	@SuppressWarnings("deprecation")

	public static StringBuffer Header()
	{	
		System.out.println("START");
		StringBuffer result1 = new StringBuffer();
		result1.append("<p><b></b>Dear Team,<br></br><br></br> Kindly,find the below GOOGLE report hourwise for today<br></br></p>");
		result1.append("<td bgcolor=\"#ffffff\" style=\"border: 1px solid black\">");
		result1.append("<p><b>AIRTEL</b></p>");
		result1.append(Hourwise(2001,"AIRTEL",0.02).toString());
		//		result1.append(getCurrentTimeStamp());
		//		result1.append("<p><b>VODAFONE</b></p>");
		//		result1.append(Hourwise(2004,"VODAFONE",0.03).toString());
		result1.append("<p><b>Note:</b>Do reply back if there is any gap in clicks/conversion </p><br></br>"
				+"Regards,<br></br>Team Globocom");
		return result1;
	}
	public static StringBuffer Hourwise(int id,String name,double cost)
	{
		Integer sum_act= 0;
		Integer sum_park=0;
		Integer sum_toalact=0;
		double sum_sdc=0;
		double sum_pdc=0;
		double sum_sdc_perc=0;
		double sum_pdc_perc=0;
		double sum_total_churm=0.0;
		Double pdc_per=0.0 ;
		Double sdc_per=0.0 ;
		Double pdc_per2=0.0 ;
		Double sdc_per2=0.0 ;
		Double total_per =0.0;
		Double total_per2 =0.0;

		StringBuffer result = new StringBuffer();
		List<Content> cont = new ArrayList<Content>();
		List<Content> opr = new ArrayList<Content>();
		List<Content> f14 = new  ArrayList<Content>();

		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con = DriverManager
					.getConnection(
							"jdbc:mysql://"+ip+":3307/mglobalV2?characterEncoding=utf-8&useUnicode=true&autoReconnect=true",
							"productionuser", "Ba5Wdnb7lNET");
			//			//			"jdbc:mysql://matrixdbinstance.cxhdhgxl5nfh.ap-southeast-1.rds.amazonaws.com:3306/mglobal","globoads","globoads");  
			//			//here sonoo is database name, root is username and password  
			Statement stmt=(Statement) con.createStatement();  
			String select_query="select date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%H') parking_Date,"
					+ "count(distinct msisdn) total_churn,case when b.pdc_count is NULL then '0' else b.pdc_count end \"PDC count\","
					+ "case when c.sdc_count is NULL then '0' else c.sdc_count end \"SDC count\" from subscription_detail a left join "
					+ "(select date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d%H') park_deact_date,count(distinct msisdn) pdc_count "
					+ "from subscription_detail "
					+ "where date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d') "
					+ "and action in ('DCT','SDCT','DEACTIVATE') and operatorid='"+id+"' and msisdn in (select distinct msisdn from subscription_detail "
					+ "where action='PARKING' and operatorid='"+id+"' and "
					+ "date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d')) group by 1) "
					+ "b on date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d%H')=b.park_deact_date "
					+ "left join (select date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d%H') same_deact_Date,count(distinct msisdn) "
					+ "sdc_count from subscription_detail where"
					+ " date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d') "
					+ "and action in ('DCT','SDCT','DEACTIVATE') and operatorid='"+id+"' and "
					+ "msisdn in (select distinct msisdn from subscription_detail where "
					+ "date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d') "
					+ "and action='ACT' and amount>0 and operatorid='"+id+"' and campaignid!=1) "
					+ "group by date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d%H')) c "
					+ "on date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d%H')=c.same_deact_Date "
					+ "where date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d')"
					+ " and a.action in ('DCT','SDCT','DEACTIVATE') and a.operatorid='"+id+"'  "
					+ "group by date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d%H');";

			ResultSet rs=((java.sql.Statement) stmt).executeQuery(select_query);  

			System.out.println(id+" :DEACTIVATION QUERY  :" + select_query);
			//			System.out.println("query1 is : "+ churn_query1);

			while(rs.next())  
			{
				Content count = new Content();
				count.setHour(rs.getInt(1));
				count.setTotal_churn(rs.getInt(2));
				count.setPdc_count(rs.getInt(3));
				count.setSdc_count(rs.getInt(4));
				cont.add(count);
			}
			//						for(Content a : cont)
			//						{
			//							System.out.println(a.getHour()+":"+a.getTotal_churn()+":"+a.getPdc_count()+":"+a.getSdc_count());
			//						}
			//			System.out.println();
			//			ResultSet off=((java.sql.Statement) stmt).executeQuery("select id from offerdetails where operatorid='"+id+"' and agencyid='2' "
			//					+ " and id not in ('1156','1160','1161');");
			//
			//			Content offer = new Content();
			//			List c1 = new ArrayList();
			//			while(off.next())
			//			{
			//				c1.add(off.getInt(1));
			//			}
			con.close(); 

			int i=0;

			while(i<24)
			{
				Content op1 = new Content();
				op1.setHour(i);
				f14.add(op1);
				i++;	
			}

			//									for (Content opt12 : f14)
			//									{
			//										System.out.println(opt12.getHour());
			//									}

			//			System.out.println("offer id : " + c1.get(0) +"," + c1.get(1) + ", " + c1.get(2)+", " + c1.get(3));


			Class.forName("com.mysql.jdbc.Driver");
			Connection con2 = DriverManager
					.getConnection(
							"jdbc:mysql://"+ip+":3307/mglobalV2?characterEncoding=utf-8&useUnicode=true&autoReconnect=true",
							"productionuser", "Ba5Wdnb7lNET");
			//			// "jdbc:mysql://matrixdbinstance.cxhdhgxl5nfh.ap-southeast-1.rds.amazonaws.com:3306/metrix","matrix","matrix1468");
			//
			//			// here sonoo is database name, root is username and password
			Statement stmt2 = (Statement) con2.createStatement();
			String act_query1 = "select date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%H') total_act_time,"
					+ "count(distinct msisdn) total_Act,case when b.act_count is NULL then '0' else b.act_count end \"Charged Act\","
					+ "case when c.park_Act_count is NULL then '0' else c.park_Act_count end \"Park Act\" "
					+ "from subscription_detail a  left join (select date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d%H') hr,"
					+ "count(1) act_count  from subscription_detail  where "
					+ "date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d') "
					+ "and action='ACT' and amount>0 and operatorid='"+id+"' group by 1) b on "
					+ "date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d%H')=b.hr "
					+ "left join (select date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d%H') parking_act_time,"
					+ "count(distinct msisdn) park_Act_count from subscription_detail where "
					+ "date_format(CONVERT_TZ(transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d') "
					+ "and action='PARKING' and operatorid='"+id+"' group by 1) c on "
					+ "date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d%H')=c.parking_act_time "
					+ "where date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d')=date_format(CONVERT_TZ(now(),'+00:00','+05:30'),'%Y%m%d') "
					+ "and a.action in ('ACT','PARKING') and a.operatorid='"+id+"' "
					+ "group by date_format(CONVERT_TZ(a.transtime,'+00:00','+05:30'),'%Y%m%d%H');";

			System.out.println("ACTIVATION QUERY"+act_query1);
			ResultSet rs2 = ((java.sql.Statement) stmt2).executeQuery(act_query1);

			List<Content> f1 = new  ArrayList<Content>();
			List<Content> f4 = new  ArrayList<Content>();
			List<Content> f5 = new  ArrayList<Content>();
			List<Content> f6 = new  ArrayList<Content>();

			while(rs2.next())
			{
				Content f0 = new Content();
				f0.setHour(rs2.getInt(1));
				f0.setTotal_act(rs2.getInt(2));
				f0.setAct(rs2.getInt(3));
				f0.setParking_act(rs2.getInt(4));
				f1.add(f0);
			}
			con2.close();
			//						for(Content at : f1)
			//						{
			//							System.out.println(at.getHour()+":"+at.getAct()+":"+at.getTotal_act()+":"+at.getParking_act());
			//						}



			for(Content pt : f14)
			{	
				Content f8 = new Content();
				f8.setTotal_act(0);
				f8.setAct(0);
				f8.setParking_act(0);
				f8.setHour(pt.getHour());
				for(Content f : f1)
				{	
					if(pt.getHour()==f.getHour())
					{

						f8.setTotal_act(f.getTotal_act());
						f8.setParking_act(f.getParking_act());
						f8.setAct(f.getAct());
						break;
					}
				}
				f4.add(f8);
			}
			//			for(Content at : f4)
			//			{
			//				System.out.println(at.getHour()+":"+at.getAct()+":"+at.getTotal_act()+":"+at.getParking_act());
			//			}


			//			for(Final at : f6)
			//			{
			//				System.out.println(at.getHour()+":"+at.getAct()+":"+at.getDct());
			//			}


			//			

			//		
			//			result.append("</table>");

			for(Content f12 : f4)
			{	
				Content oper = new Content();
				oper.setTotal_act(f12.getTotal_act());
				oper.setAct(f12.getAct());
				oper.setParking_act(f12.getParking_act());
				oper.setHour(f12.getHour());
				oper.setTotal_churn(0);
				oper.setPdc_count(0);
				oper.setSdc_count(0);
				for(Content f45 : cont)
				{	
					if(f12.getHour()==f45.getHour())
					{
						oper.setTotal_churn(f45.getTotal_churn());
						oper.setPdc_count(f45.getPdc_count());
						oper.setSdc_count(f45.getSdc_count());
						break;
					}
				}

				opr.add(oper);
			}
			//						for(Content at : opr)
			//						{
			//							System.out.println(at.getHour());
			//						}
			result.append("<table bordercolor=\"#5C594A\" border=\"3\" cellpadding=\"3\" cellspacing=\"0\" >");
			result.append("<tr bgcolor=\"#ADBCBD\"><td align=\"center\" valign=\"middle\" colspan=\"9\" font color=\"BLACK\"><b><i>"
					+" "+name +" HOUR WISE CHURN COUNT  </b></i></td></tr>");
			result.append("<tr bgcolor=\"#55E7F1\" >"
					+ "<td align=\"center\" valign=\"middle\"> Current Hour </td> "
					+ "<td align=\"center\" valign=\"middle\"> ACT </td> "
					+ "<td align=\"center\" valign=\"middle\"> Parking </td> "
					+ "<td align=\"center\" valign=\"middle\"> Total Act </td> "
					+ "<td align=\"center\" valign=\"middle\"> SDC </td> "
					+ "<td align=\"center\" valign=\"middle\"> PDC </td> "
					+ "<td align=\"center\" valign=\"middle\"> SDC % </td>"
					+ "<td align=\"center\" valign=\"middle\"> PDC % </td>"
					+ "<td align=\"center\" valign=\"middle\"> Total Churn % </td>"
					+ "</tr>");
			int k=0;
			int j=0;
			int l=0;
			for(Content op1 : opr)
			{	

				sum_act=sum_act+op1.getAct();
				sum_park=sum_park+op1.getParking_act();
				sum_toalact=sum_toalact+op1.getTotal_act();

				//				double churn_perc=0.0;


				//				sum_churnperc=sum_churnperc+churn_perc;

				DecimalFormat df = new DecimalFormat("#.##");


				if(op1.getAct().equals(0)){
					sdc_per=0.0;
				}else{
					sdc_per= (double)(op1.getSdc_count()* 100)/op1.getAct() ;
				}
				if(op1.getParking_act().equals(0)){
					pdc_per=0.0;
				}else{
					pdc_per= (double) (op1.getPdc_count()* 100)/op1.getParking_act();
				}

				Integer sum =op1.getParking_act()+op1.getAct();
				if(sum.equals(0)){
					total_per=0.0;
				}
				else{
					total_per= (double) ((op1.getPdc_count()+op1.getSdc_count())*100)/sum ;
				}

				result.append("<tr>"
						+ "<td align=\"center\" valign=\"middle\"><b>" + op1.getHour()+"</b><sup>th</sup></td>"
						+ "<td align=\"center\" valign=\"middle\">" + op1.getAct() + "</td>"
						+ "<td align=\"center\" valign=\"middle\">" + op1.getParking_act()+"</td>"
						+ "<td align=\"center\" valign=\"middle\">" + op1.getTotal_act() +"</td>"
						+"<td align=\"center\" valign=\"middle\">" + op1.getSdc_count() +"</td>"
						+"<td align=\"center\" valign=\"middle\">" + op1.getPdc_count() +"</td>"
						+ "<td align=\"center\" valign=\"middle\">"+df.format(sdc_per)+"</td>"
						+ "<td align=\"center\" valign=\"middle\">"+df.format(pdc_per)+"</td>"
						+ "<td align=\"center\" valign=\"middle\">"+df.format(total_per)+"</td>"
						+ "</tr>" );
				sum_sdc=sum_sdc+op1.getSdc_count();
				sum_pdc=sum_pdc+op1.getPdc_count();
				if(sdc_per!=0.0)
				{
					k++;
				}
				//				System.out.println("Value of K: "+k);
				if(pdc_per!=0.0)
				{
					j++;
				}
				if(total_per!=0.0)
				{
					l++;
				}

			}
			//			System.out.println("K :" + k + "J : "+ j +"L" + l);



			//			}
			double sum_sdc_perc2=0.0;
			double sum_pdc_perc2=0.0;



			if(l==0)
			{
				total_per2=0.0;
			}else{
				total_per2=(double) sum_total_churm/l;
			}

			//			System.out.println("sdc:" + sum_sdc_perc2+" pdc: "+sum_pdc_perc2+"total perc2 : "+ total_per2);
			sum_sdc_perc2=(double) (sum_sdc*100)/sum_act;
			sum_pdc_perc2=(double) (sum_pdc*100)/sum_park;
			total_per2= (double) ((sum_sdc+sum_pdc)*100)/(sum_act+sum_park) ;
			DecimalFormat df = new DecimalFormat("#.##");
			result.append("<tr bgcolor=\"#ADBCBD\">"
					+"<td align=\"center\" valign=\"middle\" colspan=\"1\"><b> TOTAL </b></td>"
					+"<td align=\"center\" valign=\"middle\" ><b> "+ sum_act +"</b></td>"
					+"<td align=\"center\" valign=\"middle\" ><b> "+ sum_park +"</b></td>"
					+"<td align=\"center\" valign=\"middle\" ><b> "+ sum_toalact +"</b></td>"
					+"<td align=\"center\" valign=\"middle\" ><b> "+ df.format(sum_sdc) +"</b></td>"
					+"<td align=\"center\" valign=\"middle\" ><b> "+ df.format(sum_pdc) +"</b></td>"	
					+"<td align=\"center\" valign=\"middle\" ><b> "+ df.format(sum_sdc_perc2) +"</b></td>"
					+"<td align=\"center\" valign=\"middle\" ><b> "+ df.format(sum_pdc_perc2) +"</b></td>"
					+"<td align=\"center\" valign=\"middle\" ><b> "+ df.format(total_per2) +" %</b></td>"
					+ "</tr>");

			result.append("</table>");
			//			System.out.println("TIME SI :" + getCurrentTimeStamp());
		}

		catch(Exception e){e.printStackTrace();System.out.println(e);result=new StringBuffer("MYSQL TIMEOUT");}
		System.out.println(" RESULE : " + result);
		return result; 


	}  

	public static Integer getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH");
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		String strDate = sdfDate.format(cal.getTime());
		//		System.out.println(strDate);
		return Integer.valueOf(strDate);
	}

	public static String getCurrentTimeStampSubject() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		String strDate = sdfDate.format(cal.getTime());
		// System.out.println(strDate);
		return strDate;
	}


}
