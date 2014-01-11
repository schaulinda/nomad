package com.nomade.domain;

import org.springframework.data.domain.Page;

public class BeanHistorique {
	
	private Page<EtapeVoyage> listEtapeVoy;
	
	private Page<EtapeVehicule> listEtapeVeh;
	
	private Page<DangerPratique> listDanger;
	
	private Page<InfoPratique> listInfo;
	
	private UserNomade nomade;
	
	private Comment comment = new Comment();

	public Page<EtapeVoyage> getListEtapeVoy() {
		return listEtapeVoy;
	}

	public void setListEtapeVoy(Page<EtapeVoyage> listEtapeVoy) {
		this.listEtapeVoy = listEtapeVoy;
	}

	public Page<EtapeVehicule> getListEtapeVeh() {
		return listEtapeVeh;
	}

	public void setListEtapeVeh(Page<EtapeVehicule> listEtapeVeh) {
		this.listEtapeVeh = listEtapeVeh;
	}

	public Page<DangerPratique> getListDanger() {
		return listDanger;
	}

	public void setListDanger(Page<DangerPratique> listDanger) {
		this.listDanger = listDanger;
	}

	public Page<InfoPratique> getListInfo() {
		return listInfo;
	}

	public void setListInfo(Page<InfoPratique> listInfo) {
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
