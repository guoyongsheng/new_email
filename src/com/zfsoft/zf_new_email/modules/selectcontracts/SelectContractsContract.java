/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectcontracts;

import java.util.ArrayList;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.ContractsInfo;

/**
 * @author wesley
 * @date: 2016-11-26
 * @Description:选择联系人的契约接口
 */
public interface SelectContractsContract {

	interface View extends BaseView<SelectContractsPresenter> {

	}

	interface Presenter extends BasePresenter {

		/**
		 * 获取联系人集合
		 * 
		 * @param list
		 *            所有的联系人
		 * @param listSelected
		 *            已选择的联系人
		 * @return 联系人集合
		 */
		ArrayList<ContractsInfo> getContractsInfoList(ArrayList<ContractsInfo> list, ArrayList<ContractsInfo> listSelected);

		/**
		 * 获取选中的联系人列表
		 * 
		 * @param list
		 *            所有的联系人列表
		 * 
		 * @return 选中的联系人列表
		 */
		ArrayList<ContractsInfo> getSelectedContractsList(ArrayList<ContractsInfo> list);

		/**
		 * 选择的联系人列表是否全部包含输入的联系人列表
		 * 
		 * @param list_selected
		 *            选择的联系人列表
		 * @param list_input
		 *            输入的联系人列表
		 * @return true：包含 false：不包含
		 */
		boolean contractsListContainsInputContracts(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input);

		/**
		 * 选择的联系人列表是否全部都不包含输入的联系人列表
		 * 
		 * @param list_selected
		 *            选择的联系人列表
		 * @param list_input
		 *            输入的联系人列表
		 * @return true：不包含 false：包含
		 */
		boolean contractsListNotContainsInputContracts(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input);

		/**
		 * 获取不包含的list
		 * 
		 * @param list_selected
		 *            选择的联系人列表
		 * @param list_input
		 *            输入的联系人列表
		 * @return 不包含的list
		 */
		ArrayList<ContractsInfo> getNotContainsList(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input);

		/**
		 * 对象转化
		 * 
		 * @param list
		 *            要转换的对象
		 * @return 对象集合
		 */
		ArrayList<String> contractsListToStringList(ArrayList<ContractsInfo> list);
	}
}
