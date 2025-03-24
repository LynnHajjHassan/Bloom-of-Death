package comp2800Labs;

/* For students to study and work on assignments and projects *
 * \\\\\ Copyright material (contact xyuan@uwindsor.ca) ///// */

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class GeoIndexedQuad extends GeometryShapes {
	
	/* a function to build a Shape3D object with one IndexedQuad */
	public static Shape3D index_QuadShape(float s) {
		int v_num = 8, s_num = 6;                          // use 'v_num' points and create 's_num' sides
		IndexedQuadArray indexQuad = new IndexedQuadArray(v_num,
				GeometryArray.COLOR_3 | GeometryArray.COORDINATES, 4 * s_num);

		int[] colorIndices = { 0, 0, 0, 0, 1, 1, 1, 1, 2,
				2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5 };
		indexQuad.setColors(0, CommonsLH.list_clrs);
		indexQuad.setColorIndices(0, colorIndices);            // use indices to set colors

		Point3f[] pts = new Point3f[v_num];                // define 'v_num' of vertices
		pts[0] = new Point3f(s *  0.25f, s *  0.25f, s *  0.25f);
		pts[1] = new Point3f(s * -0.25f, s *  0.25f, s *  0.25f);
		pts[2] = new Point3f(s *  0.25f, s *  0.25f, s * -0.25f);
		pts[3] = new Point3f(s * -0.25f, s *  0.25f, s * -0.25f);
		pts[4] = new Point3f(s *  0.5f,  s * -0.5f,  s *  0.5f);
		pts[5] = new Point3f(s * -0.5f,  s * -0.5f,  s *  0.5f);
		pts[6] = new Point3f(s *  0.5f,  s * -0.5f,  s * -0.5f);
		pts[7] = new Point3f(s * -0.5f,  s * -0.5f,  s * -0.5f);
		int[] indices = { 0, 2, 3, 1, 4, 5, 7, 6, 0, 4,
				6, 2, 1, 3, 7, 5, 1, 5, 4, 0, 2, 6, 7, 3 };
		indexQuad.setCoordinates(0, pts);                      // use indices to set coordinates
		indexQuad.setCoordinateIndices(0, indices);

		return new Shape3D(indexQuad);
	}
	
	public GeoIndexedQuad() {
		
		shapeBG.addChild(index_QuadShape(0.9f));	
	}
}
