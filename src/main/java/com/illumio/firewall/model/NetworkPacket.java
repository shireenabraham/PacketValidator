package com.illumio.firewall.model;

import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
public class NetworkPacket {
	PacketDirection direction;
	Protocol protocol;
	Range portRange;
	Range ipAddressRange;

	public static enum PacketDirection {
		INBOUND,
		OUTBOUND;

		public static PacketDirection getEnum(String value){
			if (PacketDirection.INBOUND.name().equalsIgnoreCase(value)){
				return PacketDirection.INBOUND;
			}
			return PacketDirection.OUTBOUND;
		}
	}

	public static enum Protocol {
		TCP,
		UDP;

		public static Protocol getEnum(String value){
			if (Protocol.TCP.name().equalsIgnoreCase(value)){
				return Protocol.TCP;
			}
			return Protocol.UDP;
		}
	}
}