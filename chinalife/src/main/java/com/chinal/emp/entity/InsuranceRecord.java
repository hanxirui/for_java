package com.chinal.emp.entity;

/**
  
*/
public class InsuranceRecord {
	//
	private int id;
	// 保单号
	private String baoxiandanhao;
	// 投保单号
	private String toubaodanhao;
	// 业务员代码
	private String yewuyuandaima;
	// 业务员姓名
	private String yewuyuanxingming;
	// 险种名称
	private String xianzhongmingcheng;
	// 保单状态
	private String baodanzhuangtai;
	// 投保日期
	private String toubaoriqi;
	// 生效日期
	private String shengxiaoriqi;
	// 基本保额
	private String jibenbaoe;
	// 基本保费
	private String jibenbaofei;
	// 投保人姓名
	private String toubaorenxingming;
	// 投保人性别
	private String toubaorenxingbie;
	// 投保人身份证号
	private String toubaorenshenfenzhenghao;
	// 投保人手机号
	private String toubaorenshoujihao;
	// 投保人通讯地址
	private String toubaorentongxundizhi;
	// 投保人职业
	private String toubaorenzhiye;
	// 被保人姓名
	private String beibaoxianrenxingming;
	// 被保险人性别
	private String beibaoxianrenxingbie;
	// 被保险人身份证号
	private String beibaoxianrenshenfenzhenghao;
	// 被保险人手机号
	private String beibaoxianrenshoujihao;
	// 被保险人通讯地址
	private String beibaoxianrentongxundizhi;
	// 被保险人职业
	private String beibaoxianrenzhiye;
	// 被保险人与投保人关系
	private String beibaoxianrenyutoubaorenguanxi;
	// 受益人姓名
	private String shouyirenxingming;
	// 受益人性别
	private String shouyirenxingbie;
	// 受益人身份证号
	private String shouyirenshenfenzhenghao;
	// 受益顺序
	private String shouyishunxu;
	// 受益份额
	private String shouyifene;
	// 受益人与投保人关系
	private String shouyirenyutoubaorenguanxi;
	// 缴费期
	private String jiaofeiqi;
	// 保险期
	private String baoxianqi;
	// 缴费银行
	private String jiaofeiyinhang;
	// 缴费账号
	private String jiaofeizhanghao;

	// 机构号
	private String jigouhao;
	// 险种代码
	private String xianzhongdaima;
	// 渠道
	private String qudao;
	// 支行
	private String zhihang;
	// 网点
	private String wangdian;
	// 电话
	private String dianhua;
	// 出生日期
	private String chushengriqi;
	// 部门经理
	private String bumengjingli;
	// 原专管员
	private String yuanzhuanguanyuan;
	// 新分配人员
	private String xinfenpeirenyuan;
	// 发放时间
	private String fafangshijian;
	// 发放标识
	private String fafangbiaoshi;
	// 初始来源
	private String chushilaiyuan;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setBaoxiandanhao(String baoxiandanhao) {
		this.baoxiandanhao = baoxiandanhao;
	}

	public String getBaoxiandanhao() {
		return this.baoxiandanhao;
	}

	public void setToubaodanhao(String toubaodanhao) {
		this.toubaodanhao = toubaodanhao;
	}

	public String getToubaodanhao() {
		return this.toubaodanhao;
	}

	public void setYewuyuandaima(String yewuyuandaima) {
		this.yewuyuandaima = yewuyuandaima;
	}

	public String getYewuyuandaima() {
		return this.yewuyuandaima;
	}

	public void setYewuyuanxingming(String yewuyuanxingming) {
		this.yewuyuanxingming = yewuyuanxingming;
	}

	public String getYewuyuanxingming() {
		return this.yewuyuanxingming;
	}

	public void setXianzhongmingcheng(String xianzhongmingcheng) {
		this.xianzhongmingcheng = xianzhongmingcheng;
	}

	public String getXianzhongmingcheng() {
		return this.xianzhongmingcheng;
	}

	public void setBaodanzhuangtai(String baodanzhuangtai) {
		this.baodanzhuangtai = baodanzhuangtai;
	}

	public String getBaodanzhuangtai() {
		return this.baodanzhuangtai;
	}

	public void setToubaoriqi(String toubaoriqi) {
		this.toubaoriqi = toubaoriqi;
	}

	public String getToubaoriqi() {
		return this.toubaoriqi;
	}

	public void setShengxiaoriqi(String shengxiaoriqi) {
		this.shengxiaoriqi = shengxiaoriqi;
	}

	public String getShengxiaoriqi() {
		return this.shengxiaoriqi;
	}

	public void setJibenbaoe(String jibenbaoe) {
		this.jibenbaoe = jibenbaoe;
	}

	public String getJibenbaoe() {
		return this.jibenbaoe;
	}

	public void setJibenbaofei(String jibenbaofei) {
		this.jibenbaofei = jibenbaofei;
	}

	public String getJibenbaofei() {
		return this.jibenbaofei;
	}

	public void setToubaorenxingming(String toubaorenxingming) {
		this.toubaorenxingming = toubaorenxingming;
	}

	public String getToubaorenxingming() {
		return this.toubaorenxingming;
	}

	public void setToubaorenxingbie(String toubaorenxingbie) {
		this.toubaorenxingbie = toubaorenxingbie;
	}

	public String getToubaorenxingbie() {
		return this.toubaorenxingbie;
	}

	public void setToubaorenshenfenzhenghao(String toubaorenshenfenzhenghao) {
		this.toubaorenshenfenzhenghao = toubaorenshenfenzhenghao;
	}

	public String getToubaorenshenfenzhenghao() {
		return this.toubaorenshenfenzhenghao;
	}

	public void setToubaorenshoujihao(String toubaorenshoujihao) {
		this.toubaorenshoujihao = toubaorenshoujihao;
	}

	public String getToubaorenshoujihao() {
		return this.toubaorenshoujihao;
	}

	public void setToubaorentongxundizhi(String toubaorentongxundizhi) {
		this.toubaorentongxundizhi = toubaorentongxundizhi;
	}

	public String getToubaorentongxundizhi() {
		return this.toubaorentongxundizhi;
	}

	public void setToubaorenzhiye(String toubaorenzhiye) {
		this.toubaorenzhiye = toubaorenzhiye;
	}

	public String getToubaorenzhiye() {
		return this.toubaorenzhiye;
	}

	public void setBeibaoxianrenxingming(String beibaoxianrenxingming) {
		this.beibaoxianrenxingming = beibaoxianrenxingming;
	}

	public String getBeibaoxianrenxingming() {
		return this.beibaoxianrenxingming;
	}

	public void setBeibaoxianrenxingbie(String beibaoxianrenxingbie) {
		this.beibaoxianrenxingbie = beibaoxianrenxingbie;
	}

	public String getBeibaoxianrenxingbie() {
		return this.beibaoxianrenxingbie;
	}

	public void setBeibaoxianrenshenfenzhenghao(String beibaoxianrenshenfenzhenghao) {
		this.beibaoxianrenshenfenzhenghao = beibaoxianrenshenfenzhenghao;
	}

	public String getBeibaoxianrenshenfenzhenghao() {
		return this.beibaoxianrenshenfenzhenghao;
	}

	public void setBeibaoxianrenshoujihao(String beibaoxianrenshoujihao) {
		this.beibaoxianrenshoujihao = beibaoxianrenshoujihao;
	}

	public String getBeibaoxianrenshoujihao() {
		return this.beibaoxianrenshoujihao;
	}

	public void setBeibaoxianrentongxundizhi(String beibaoxianrentongxundizhi) {
		this.beibaoxianrentongxundizhi = beibaoxianrentongxundizhi;
	}

	public String getBeibaoxianrentongxundizhi() {
		return this.beibaoxianrentongxundizhi;
	}

	public void setBeibaoxianrenzhiye(String beibaoxianrenzhiye) {
		this.beibaoxianrenzhiye = beibaoxianrenzhiye;
	}

	public String getBeibaoxianrenzhiye() {
		return this.beibaoxianrenzhiye;
	}

	public void setBeibaoxianrenyutoubaorenguanxi(String beibaoxianrenyutoubaorenguanxi) {
		this.beibaoxianrenyutoubaorenguanxi = beibaoxianrenyutoubaorenguanxi;
	}

	public String getBeibaoxianrenyutoubaorenguanxi() {
		return this.beibaoxianrenyutoubaorenguanxi;
	}

	public void setShouyirenxingming(String shouyirenxingming) {
		this.shouyirenxingming = shouyirenxingming;
	}

	public String getShouyirenxingming() {
		return this.shouyirenxingming;
	}

	public void setShouyirenxingbie(String shouyirenxingbie) {
		this.shouyirenxingbie = shouyirenxingbie;
	}

	public String getShouyirenxingbie() {
		return this.shouyirenxingbie;
	}

	public void setShouyirenshenfenzhenghao(String shouyirenshenfenzhenghao) {
		this.shouyirenshenfenzhenghao = shouyirenshenfenzhenghao;
	}

	public String getShouyirenshenfenzhenghao() {
		return this.shouyirenshenfenzhenghao;
	}

	public void setShouyishunxu(String shouyishunxu) {
		this.shouyishunxu = shouyishunxu;
	}

	public String getShouyishunxu() {
		return this.shouyishunxu;
	}

	public void setShouyifene(String shouyifene) {
		this.shouyifene = shouyifene;
	}

	public String getShouyifene() {
		return this.shouyifene;
	}

	public void setShouyirenyutoubaorenguanxi(String shouyirenyutoubaorenguanxi) {
		this.shouyirenyutoubaorenguanxi = shouyirenyutoubaorenguanxi;
	}

	public String getShouyirenyutoubaorenguanxi() {
		return this.shouyirenyutoubaorenguanxi;
	}

	public void setJiaofeiqi(String jiaofeiqi) {
		this.jiaofeiqi = jiaofeiqi;
	}

	public String getJiaofeiqi() {
		return this.jiaofeiqi;
	}

	public void setBaoxianqi(String baoxianqi) {
		this.baoxianqi = baoxianqi;
	}

	public String getBaoxianqi() {
		return this.baoxianqi;
	}

	public void setJiaofeiyinhang(String jiaofeiyinhang) {
		this.jiaofeiyinhang = jiaofeiyinhang;
	}

	public String getJiaofeiyinhang() {
		return this.jiaofeiyinhang;
	}

	public void setJiaofeizhanghao(String jiaofeizhanghao) {
		this.jiaofeizhanghao = jiaofeizhanghao;
	}

	public String getJiaofeizhanghao() {
		return this.jiaofeizhanghao;
	}

	public void setChushilaiyuan(String chushilaiyuan) {
		this.chushilaiyuan = chushilaiyuan;
	}

	public String getChushilaiyuan() {
		return this.chushilaiyuan;
	}

	public void setJigouhao(String jigouhao) {
		this.jigouhao = jigouhao;
	}

	public String getJigouhao() {
		return this.jigouhao;
	}

	public void setXianzhongdaima(String xianzhongdaima) {
		this.xianzhongdaima = xianzhongdaima;
	}

	public String getXianzhongdaima() {
		return this.xianzhongdaima;
	}

	public void setQudao(String qudao) {
		this.qudao = qudao;
	}

	public String getQudao() {
		return this.qudao;
	}

	public void setZhihang(String zhihang) {
		this.zhihang = zhihang;
	}

	public String getZhihang() {
		return this.zhihang;
	}

	public void setWangdian(String wangdian) {
		this.wangdian = wangdian;
	}

	public String getWangdian() {
		return this.wangdian;
	}

	public void setDianhua(String dianhua) {
		this.dianhua = dianhua;
	}

	public String getDianhua() {
		return this.dianhua;
	}

	public void setChushengriqi(String chushengriqi) {
		this.chushengriqi = chushengriqi;
	}

	public String getChushengriqi() {
		return this.chushengriqi;
	}

	public void setBumengjingli(String bumengjingli) {
		this.bumengjingli = bumengjingli;
	}

	public String getBumengjingli() {
		return this.bumengjingli;
	}

	public void setYuanzhuanguanyuan(String yuanzhuanguanyuan) {
		this.yuanzhuanguanyuan = yuanzhuanguanyuan;
	}

	public String getYuanzhuanguanyuan() {
		return this.yuanzhuanguanyuan;
	}

	public void setXinfenpeirenyuan(String xinfenpeirenyuan) {
		this.xinfenpeirenyuan = xinfenpeirenyuan;
	}

	public String getXinfenpeirenyuan() {
		return this.xinfenpeirenyuan;
	}

	public void setFafangshijian(String fafangshijian) {
		this.fafangshijian = fafangshijian;
	}

	public String getFafangshijian() {
		return this.fafangshijian;
	}

	public void setFafangbiaoshi(String fafangbiaoshi) {
		this.fafangbiaoshi = fafangbiaoshi;
	}

	public String getFafangbiaoshi() {
		return this.fafangbiaoshi;
	}

}