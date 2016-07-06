package com.hxr.javatone.xstream.nc;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias("NC")
public class NC {

	public static List<String> xmls = new ArrayList<String>();

	@XStreamAlias("HEAD")
	private Head head;

	@XStreamAlias("BODY")
	private Body body;

	public Head getHead() {
		return head;
	}

	public void setHead(final Head head) {
		this.head = head;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(final Body body) {
		this.body = body;
	}

	public static void main(final String[] args) {
		getNcs();
	}

	public static void getNcs() {
		String xmlStr = "<?xml version='1.0' encoding='UTF-8'?><NC><HEAD><NAME>db2inst1_SDNXCSR</NAME><IP>51.98.1.108</IP></HEAD><BODY><TS><TN><H>SYSCATSPACE</H><T>DMS</T><A>256</A><U>252</U></TN><TN><H>TEMPSPACE1</H><T>SMS</T><A>0</A><U>0</U></TN><TN><H>SDNXCSRUSR</H><T>DMS</T><A>81920</A><U>13923</U></TN><TN><H>SDNXCSRTMP</H><T>DMS</T><A>10240</A><U>0</U></TN><TN><H>SDNXCSRIDX</H><T>DMS</T><A>51200</A><U>3164</U></TN><TN><H>SYSTOOLSPACE</H><T>DMS</T><A>32</A><U>1</U></TN><TN><H>SYSTOOLSTMPSPACE</H><T>SMS</T><A>0</A><U>0</U></TN><TN><H>EOSWFCTX</H><T>DMS</T><A>20480</A><U>70</U></TN><TN><H>EOSTMP</H><T>DMS</T><A>10240</A><U>2</U></TN><TN><H>UNIFAPPUSR</H><T>DMS</T><A>20480</A><U>1616</U></TN><TN><H>UNIFAPPIDX</H><T>DMS</T><A>10240</A><U>1441</U></TN><TN><H>CSRAPPUSR</H><T>DMS</T><A>20480</A><U>160</U></TN><TN><H>CSRAPPIDX</H><T>DMS</T><A>10240</A><U>113</U></TN><TN><H>CSRDICTUSR</H><T>DMS</T><A>5120</A><U>54</U></TN><TN><H>total</H><T>DMS</T><A>240928</A><U>20798</U></TN></TS><SINFO><TN><S>ConnectCompleted</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164854</HN><DB>SDNXCSR</DB><P>db2fw6</P></TN><TN><S>UOWWaiting</S><N>CSR</N><HN>J3620168.A44F.161005114256</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.106.45781.160905130409</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164858</HN><DB>SDNXCSR</DB><P>db2fw10</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164915</HN><DB>SDNXCSR</DB><P>db2fw27</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47541.161005150638</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164902</HN><DB>SDNXCSR</DB><P>db2fw14</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47538.161005150636</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47543.161005150640</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164847</HN><DB>SDNXCSR</DB><P>db2lused</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.db2inst1.161005150645</HN><DB>SDNXCSR</DB><P>db2bp</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164852</HN><DB>SDNXCSR</DB><P>db2fw4</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43883.160904165104</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164853</HN><DB>SDNXCSR</DB><P>db2fw5</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43888.160904165109</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164903</HN><DB>SDNXCSR</DB><P>db2fw15</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47540.161005150637</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164912</HN><DB>SDNXCSR</DB><P>db2fw24</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164848</HN><DB>SDNXCSR</DB><P>db2fw0</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164907</HN><DB>SDNXCSR</DB><P>db2fw19</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47535.161005150633</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164855</HN><DB>SDNXCSR</DB><P>db2fw7</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164914</HN><DB>SDNXCSR</DB><P>db2fw26</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164857</HN><DB>SDNXCSR</DB><P>db2fw9</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>J362016B.AA32.160904164932</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164909</HN><DB>SDNXCSR</DB><P>db2fw21</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47537.161005150635</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>J3620168.A5AA.160927173752</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43885.160904165106</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164844</HN><DB>SDNXCSR</DB><P>db2stmm</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.43737.160904164942</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164856</HN><DB>SDNXCSR</DB><P>db2fw8</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47544.161005150641</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164916</HN><DB>SDNXCSR</DB><P>db2evmg_DB2DETAILDEA</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164911</HN><DB>SDNXCSR</DB><P>db2fw23</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164913</HN><DB>SDNXCSR</DB><P>db2fw25</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43890.160904165111</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164846</HN><DB>SDNXCSR</DB><P>db2wlmd</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164850</HN><DB>SDNXCSR</DB><P>db2fw2</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47542.161005150639</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43884.160904165105</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164908</HN><DB>SDNXCSR</DB><P>db2fw20</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43887.160904165108</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164851</HN><DB>SDNXCSR</DB><P>db2fw3</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43886.160904165107</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164859</HN><DB>SDNXCSR</DB><P>db2fw11</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.106.48611.160904164930</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43891.160904165112</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164904</HN><DB>SDNXCSR</DB><P>db2fw16</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164900</HN><DB>SDNXCSR</DB><P>db2fw12</P></TN><TN><S>Connect"
				+ "Completed</S><N>CSR</N><HN>51.98.1.108.47536.161005150634</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43889.160904165110</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164849</HN><DB>SDNXCSR</DB><P>db2fw1</P></TN><TN><S>UOW"
				+ "Waiting</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164845</HN><DB>SDNXCSR</DB><P>db2taskd</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164905</HN><DB>SDNXCSR</DB><P>db2fw17</P></TN><TN><S>UOW"
				+ "Waiting</S><N>CSR</N><HN>51.98.1.108.43892.160904165113</HN><DB>SDNXCSR</DB><P>db2jcc_application</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164901</HN><DB>SDNXCSR</DB><P>db2fw13</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164910</HN><DB>SDNXCSR</DB><P>db2fw22</P></TN><TN><S>Connect"
				+ "Completed</S><N>DB2INST1</N><HN>*LOCAL.DB2.160904164906</HN><DB>SDNXCSR</DB><P>db2fw18</P></TN></SINFO><LINFO><TN><H>SDNXCSR</H><T>-</T><DB>-</DB><SC>0</SC><TNA>3</TNA><LO>1</LO><LSC>DB2INST1</LSC><LS>db2bp</LS><U>*LOCAL.db2inst1.161005150700</U><P>2016-07-05-12.06.02.136978</P></TN><TN><H>SDNXCSR</H><T>-</T><DB>-</DB><SC>0</SC><TNA>3</TNA><LO>1</LO><LSC>DB2INST1</LSC><LS>db2bp</LS><U>*LOCAL.db2inst1.161005150700</U><P>2016-07-05-12.06.02.136978</P></TN></LINFO><CINFO><TN><N>IBMDEFAULTBP</N><P>10000</P><PS>8</PS><AHR>100</AHR><DHR>100</DHR><IHR>100</IHR><SHR>0</SHR><RUT>0</RUT></TN><TN><N>CSR_BUF</N><P>131072</P><PS>8</PS><AHR>100</AHR><DHR>100</DHR><IHR>100</IHR><SHR>0</SHR><RUT>0</RUT></TN><TN><N>EOSBP32K</N><P>32768</P><PS>32</PS><AHR>100</AHR><DHR>100</DHR><IHR>100</IHR><SHR>0</SHR><RUT>0</RUT></TN><TN><N>UNIFAPP_BUF</N><P>327680</P><PS>8</PS><AHR>100</AHR><DHR>100</DHR><IHR>100</IHR><SHR>0</SHR><RUT>0</RUT></TN><TN><N>CSRAPP_BUF</N><P>131072</P><PS>8</PS><AHR>100</AHR><DHR>100</DHR><IHR>100</IHR><SHR>0</SHR><RUT>0</RUT></TN><TN><N>CSRDICT_BUF</N><P>12800</P><PS>8</PS><AHR>100</AHR><DHR>100</DHR><IHR>100</IHR><SHR>0</SHR><RUT>0</RUT></TN></CINFO></BODY></NC>";
		NC nc = toBean(xmlStr, NC.class);

		String xmlStr1 = "<?xml version='1.0'encoding='UTF-8'?><NC><HEAD><NAME>51.96.12.16呼叫中心、绩效考核</NAME><IP>51.96.12.16</IP></HEAD><BODY><CMS><C>0.1200</C><MA>33554432</MA><M>0.8099</M><SA>67108864</SA><S>0.2500</S></CMS><DISC><TN><H>/dev/csrhislv</H><A>153600</A><U>134768</U></TN><TN><H>/dev/hd4</H><A>2048</A><U>1227.79</U></TN><TN><H>/dev/hd2</H><A>5120</A><U>3450.37</U></TN><TN><H>/dev/hd9var</H><A>5120</A><U>4740.82</U></TN><TN><H>/dev/hd3</H><A>5120</A><U>3801.5</U></TN><TN><H>/dev/hd1</H><A>15360</A><U>10903.4</U></TN><TN><H>/dev/hd10opt</H><A>6144</A><U>5032.25</U></TN><TN><H>/dev/webspherelv</H><A>51200</A><U>4025.38</U></TN><TN><H>/dev/ptflv</H><A>204800</A><U>108270</U></TN><TN><H>/dev/hdmsapplv</H><A>71680</A><U>61579.4</U></TN><TN><H>/dev/csrlv</H><A>36864</A><U>32347.4</U></TN><TN><H>/dev/jxdatalv</H><A>307200</A><U>280200</U></TN><TN><H>/dev/lv_log</H><A>51200</A><U>20930.7</U></TN><TN><H>/dev/fslv00</H><A>20480</A><U>5035.03</U></TN><TN><H>/dev/fslv01</H><A>10240</A><U>5131.66</U></TN><TN><H>/dev/fslv02</H><A>163840</A><U>163840</U></TN><TN><H>/dev/lv_crshis_home</H><A>20480</A><U>1417.93</U></TN><TN><H>/dev/cc_dblv</H><A>122880</A><U>102565</U></TN><TN><H>/dev/fslv03</H><A>15360</A><U>258.711</U></TN><TN><H>/dev/fslv04</H><A>15360</A><U>15203.3</U></TN><TN><H>total</H><A>1284096</A><U>964728</U></TN></DISC></BODY></NC>";
		NC nc1 = toBean(xmlStr1, NC.class);

	}

	public static String toXml(final Object obj) {
		XStream xstream = new XStream();
		xstream.processAnnotations(obj.getClass());
		return xstream.toXML(obj);
	}

	public static <T> T toBean(final String xmlStr, final Class<T> cls) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(cls);
		T obj = (T) xstream.fromXML(xmlStr);
		return obj;
	}

}
