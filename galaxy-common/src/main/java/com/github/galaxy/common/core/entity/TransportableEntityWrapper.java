package com.github.galaxy.common.core.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

// 用于传输实例的网络传输
@Slf4j
public abstract class TransportableEntityWrapper {

	@Getter
	@Setter
	private TransportableEntity entity;

	public abstract byte[] writeObject();

	public abstract TransportableEntity readObject(byte[] context);
}
