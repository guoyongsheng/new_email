package com.zfsoft.zf_new_email.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author wesley
 * @date 2017-2-22
 * @Description: oa联系人对象
 */
public class InnerContractsInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // 联系人id
	private String sum; // 联系人总数
	private String name; // 联系人的名字

	private int checkMode; // 选中模式 0:未选中 1:选中部分 2:全部选中
	private boolean isExpanded; // 是否展开

	private List<InnerContractsInfo> childList; // 子节点数据集合
	private boolean hasRootNode; // 是否有根节点
	private InnerContractsInfo parentNode; // 父节点

	private List<InnerContractsInfo> childIdList; // 子节点id数据集合

	private boolean isParentNote; // 是否是根节点

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCheckMode() {
		return checkMode;
	}

	public void setCheckMode(int checkMode) {
		this.checkMode = checkMode;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public List<InnerContractsInfo> getChildList() {
		return childList;
	}

	public void setChildList(List<InnerContractsInfo> childList) {
		this.childList = childList;
	}

	public boolean isHasRootNode() {
		return hasRootNode;
	}

	public void setHasRootNode(boolean hasRootNode) {
		this.hasRootNode = hasRootNode;
	}

	public InnerContractsInfo getParentNode() {
		return parentNode;
	}

	public void setParentNode(InnerContractsInfo parentNode) {
		this.parentNode = parentNode;
	}

	public List<InnerContractsInfo> getChildIdList() {
		return childIdList;
	}

	public void setChildIdList(List<InnerContractsInfo> childIdList) {
		this.childIdList = childIdList;
	}

	public boolean isParentNote() {
		return isParentNote;
	}

	public void setParentNote(boolean isParentNote) {
		this.isParentNote = isParentNote;
	}
}
