/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectcontracts;

import java.util.ArrayList;

import com.zfsoft.zf_new_email.entity.ContractsInfo;

/**
 * @author wesley
 * @date: 2016-11-26
 * @Description:业务逻辑
 */
public class SelectContractsPresenter implements SelectContractsContract.Presenter {

	private SelectContractsContract.View view;

	public SelectContractsPresenter(SelectContractsContract.View view) {
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public ArrayList<ContractsInfo> getContractsInfoList(ArrayList<ContractsInfo> list, ArrayList<ContractsInfo> listSelected) {

		if (listSelected == null || listSelected.size() == 0) {
			return list;
		}

		if (list == null || list.size() == 0) {
			return list;
		}

		int size = list.size();
		int sizeSelected = listSelected.size();
		for (int i = 0; i < size; i++) {
			String address = list.get(i).getEmailAddress();
			for (int j = 0; j < sizeSelected; j++) {
				String addressSelected = listSelected.get(j).getEmailAddress();
				if (addressSelected != null && addressSelected.equals(address)) {
					list.get(i).setSelected(true);
					break;
				}
			}
		}
		return list;
	}

	@Override
	public ArrayList<ContractsInfo> getSelectedContractsList(ArrayList<ContractsInfo> list) {
		ArrayList<ContractsInfo> list_selected = new ArrayList<>();
		if (list == null || list.size() == 0) {
			return list_selected;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			boolean isChecked = list.get(i).isSelected();
			if (isChecked) {
				list_selected.add(list.get(i));
			}
		}
		return list_selected;
	}

	@Override
	public boolean contractsListContainsInputContracts(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input) {

		if (list_selected == null || list_selected.size() == 0) {
			return false;
		}

		if (list_input == null || list_input.size() == 0) {
			return true;
		}

		ArrayList<String> stringSelect = contractsListToStringList(list_selected);
		ArrayList<String> stringInput = contractsListToStringList(list_input);
		return stringSelect.containsAll(stringInput);
	}

	@Override
	public boolean contractsListNotContainsInputContracts(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input) {
		if (list_selected == null || list_selected.size() == 0) {
			return true;
		}

		if (list_input == null || list_input.size() == 0) {
			return true;
		}

		ArrayList<String> stringSelect = contractsListToStringList(list_selected);
		ArrayList<String> stringInput = contractsListToStringList(list_input);
		int sizeInput = stringInput.size();
		for (int i = 0; i < sizeInput; i++) {
			if (stringSelect.contains(stringInput.get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ArrayList<ContractsInfo> getNotContainsList(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input) {

		ArrayList<ContractsInfo> list = new ArrayList<>();
		if (list_selected == null || list_selected.size() == 0) {
			return list;
		}

		if (list_input == null || list_input.size() == 0) {
			return list;
		}

		ArrayList<ContractsInfo> listCommon = new ArrayList<>();
		int size_selected = list_selected.size();
		int size_input = list_input.size();
		for (int i = 0; i < size_selected; i++) {
			String value = list_selected.get(i).getEmailAddress();
			for (int j = 0; j < size_input; j++) {
				String address = list_input.get(j).getEmailAddress();
				if (address != null && address.equals(value)) {
					listCommon.add(list_input.get(j));
					break;
				}
			}
		}
		list_input.removeAll(listCommon);
		return list_input;
	}

	@Override
	public ArrayList<String> contractsListToStringList(ArrayList<ContractsInfo> list) {
		ArrayList<String> listString = new ArrayList<>();
		if (list == null) {
			return listString;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			listString.add(list.get(i).getEmailAddress());
		}
		return listString;
	}

	@Override
	public void cancelRequest() {

	}
}
