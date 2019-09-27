package org.bwyou.springboot2.models;

import java.time.LocalDateTime;

public interface ICUModel {
	LocalDateTime getCreateDT();
	void setCreateDT(LocalDateTime createDT);
	LocalDateTime getUpdateDT();
	void setUpdateDT(LocalDateTime updateDT);
}
