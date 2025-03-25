package CodesML2800;

/* Copyright material for students working on assignments and projects */

import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.TriangleStripArray;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;

public class L3DiskML extends GroupObjects {
	private static float currentRadius = 2.0f;  // Add this line to track the current radius

	private final static int MAX_PTS = 60;
	
	public static Shape3D ring_Side(float r) {
		currentRadius = r;
		
		int k, n = MAX_PTS;
		
		int s_num = 3;                                     // make two of the approximated (curve) surfaces
		int v_num = (MAX_PTS + 1) * 2;                     // use 'MAX_PTS+1' points on two circles for the surface
		int vn_count[] = {v_num, v_num, v_num};                   // set point counters for the surface
		Point3f[] v_cdnts = new Point3f[v_num * s_num];    // allocate 3D coordinates for all surface points
		Color3f[] v_clrs = new Color3f[v_num * s_num];     //         ... colors ...
                                                           // prepare points on the two circles
		Point3f c_pts1[] = L2StarML.circle_Points(-0.1f, r, MAX_PTS);
		Point3f c_pts2[] = L2StarML.circle_Points(0.1f, r, MAX_PTS);
		Point3f ctr_pt = new Point3f(0f, 0f, -0.1f); 
		Point3f ctr_pt2 = new Point3f(0f, 0f, 0.1f); 

		for (int i = 0; i <= n; i++) {
			k = (i < n) ? i : 0;                           // NOTE: set the last two points as the first two points
			
			// set for top (flat, circular) surface
			v_cdnts[i * 2] = ctr_pt;                   
	        v_cdnts[i * 2 + 1] = c_pts1[k];           
	        v_clrs[i * 2] = v_clrs[i * 2 + 1] = CommonsML.Orange;

	        //top circle, clockwise winding
	        v_cdnts[v_num + i * 2] = c_pts2[k];       
	        v_cdnts[v_num + i * 2 + 1] = ctr_pt2;      
	        v_clrs[v_num + i * 2] = v_clrs[v_num + i * 2 + 1] = CommonsML.Red;

	        //vertical curved surface
	        v_cdnts[2 * v_num + i * 2] = c_pts1[k];
	        v_cdnts[2 * v_num + i * 2 + 1] = c_pts2[k];
	        v_clrs[2 * v_num + i * 2] = v_clrs[2 * v_num + i * 2 + 1] = CommonsML.Green;

		}
		                                                   // set geometry with the two surfaces
		TriangleStripArray object_geometry = new TriangleStripArray(v_cdnts.length, 
				TriangleStripArray.COORDINATES | TriangleStripArray.COLOR_3, vn_count);
		object_geometry.setStripVertexCounts(vn_count);    // create the object as a TriangleStripArray
		object_geometry.setCoordinates(0, v_cdnts); 
		object_geometry.setColors(0, v_clrs); 
		
		return new Shape3D(object_geometry);               // return Shape3D defined by its geometry
	}
	
	public static float getRadius() {
        return currentRadius;  // Add a method to get the current radius
    }
	
	public L3DiskML(float r) {
		super(ring_Side(r));
	}
}
