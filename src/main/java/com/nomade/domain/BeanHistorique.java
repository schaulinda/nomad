package com.nomade.domain;

import java.util.List;

import org.springframework.data.domain.Page;

public class BeanHistorique {
	
	private List<EtapeVoyage> listEtapeVoy;
	
	private List<DangerPratique> listDanger;
	
	private List<InfoPratique> listInfo;
	
	private UserNomade nomade;
	
	private Comment comment = new Comment();

	public List<EtapeVoyage> getListEtapeVoy() {
		return listEtapeVoy;
	}

	public void setListEtapeVoy(List<EtapeVoyage> listEtapeVoy) {
		this.listEtapeVoy = listEtapeVoy;
	}

	public List<DangerPratique> getListDanger() {
		return listDanger;
	}

	public void setListDanger(List<DangerPratique> listDanger) {
		this.listDanger = listDanger;
	}

	public List<InfoPratique> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<InfoPratique> listInfo) {
		this.listInfo = listInfo;
	}

	public UserNomade getNomade() {
		return nomade;
	}

	public void setNomade(UserNomade nomade) {
		this.nomade = nomade;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

}
