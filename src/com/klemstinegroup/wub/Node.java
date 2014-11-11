package com.klemstinegroup.wub;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Node {
	Rectangle2D.Double rect;
	BufferedImage image;
	AudioObject ao;

	public Node(Rectangle2D.Double playFieldPosition,
			AudioObject ao) {
		this.rect = playFieldPosition;
		this.ao = ao;
	}
}