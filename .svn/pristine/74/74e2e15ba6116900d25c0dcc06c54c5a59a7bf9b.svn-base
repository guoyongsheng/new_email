package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import java.util.List;

import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * 
 * @author wesley
 * @date 2017-2-22
 * @Description: 子节点的接口
 */
public interface SelectInnerContactChildService {

	/**
	 * 获取下一级的数据
	 * 
	 * @param info
	 *            对象
	 * @param name
	 *            名字
	 * @param sum
	 *            数量
	 * @param url
	 *            网址
	 * @param token
	 *            登陆凭证
	 * 
	 * @param listener
	 *            回调接口
	 */
	void loadChildData(InnerContractsInfo info, String name, String sum, String url, String token, CallBackListener<List<InnerContractsInfo>> listener);
}
