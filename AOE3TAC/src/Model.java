import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.lwjgl.opengl.GL11;

public class Model { // major credit to Jeremy Adams(elias4444)

	private Model() {
		
	}
	
	private static HashMap<String, Model> models = new HashMap<>();
	
	public static Model getModel(String path) {
		if (models.containsKey(path)) {
			return models.get(path);
		}
		return fromFile(path);
	}
	
	private ArrayList<float[]> vertex = new ArrayList<>();
	private ArrayList<float[]> normal = new ArrayList<>();
	private ArrayList<float[]> tex = new ArrayList<>();
	private ArrayList<int[]> face = new ArrayList<>();
	private ArrayList<int[]> facetex = new ArrayList<>();
	private ArrayList<int[]> facenorm = new ArrayList<>();
	private int glcall = 0;
	public float top = 0; // y+
	public float bottom = 0; // y-
	public float left = 0; // x-
	public float right = 0; // x+
	public float far = 0; // z-
	public float near = 0; // z+
	
	public static Model fromFile(String path) {
		Model model = new Model();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(Start.path + "model\\" + path.replace("/", "\\") + ".obj"));
			boolean first = true;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {
					float[] coords = new float[4];
					String[] coordstext = new String[4];
					coordstext = line.split("\\s+");
					for (int i = 1; i < coordstext.length; i++) {
						coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
					}
					if (first) {
						model.right = coords[0];
						model.left = coords[0];
						model.top = coords[1];
						model.bottom = coords[1];
						model.near = coords[2];
						model.far = coords[2];
						first = false;
					}
					if (coords[0] > model.right) {
						model.right = coords[0];
					}
					if (coords[0] < model.left) {
						model.left = coords[0];
					}
					if (coords[1] > model.top) {
						model.top = coords[1];
					}
					if (coords[1] < model.bottom) {
						model.bottom = coords[1];
					}
					if (coords[2] > model.near) {
						model.near = coords[2];
					}
					if (coords[2] < model.far) {
						model.far = coords[2];
					}
					model.vertex.add(coords);
				}else if (line.charAt(0) == 'v' && line.charAt(1) == 't') {
					float[] coords = new float[4];
					String[] coordstext = new String[4];
					coordstext = line.split("\\s+");
					for (int i = 1; i < coordstext.length; i++) {
						coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
					}
					model.tex.add(coords);
				}else if (line.charAt(0) == 'v' && line.charAt(1) == 't') {
					float[] coords = new float[4];
					String[] coordstext = new String[4];
					coordstext = line.split("\\s+");
					for (int i = 1; i < coordstext.length; i++) {
						coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
					}
					model.tex.add(coords);
				}else if (line.charAt(0) == 'v' && line.charAt(1) == 'n') {
					float[] coords = new float[4];
					String[] coordstext = new String[4];
					coordstext = line.split("\\s+");
					for (int i = 1; i < coordstext.length; i++) {
						coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
					}
					model.normal.add(coords);
				}else if (line.charAt(0) == 'f' && line.charAt(1) == ' ') {
					String[] coordstext = line.split("\\s+");
					int[] v = new int[coordstext.length - 1];
					int[] vt = new int[coordstext.length - 1];
					int[] vn = new int[coordstext.length - 1];
					
					for (int i = 1; i < coordstext.length; i++) {
						String fixstring = coordstext[i].replaceAll("//", "/0/");
						String[] tempstring = fixstring.split("/");
						v[i - 1] = Integer.valueOf(tempstring[0]).intValue();
						if (tempstring.length > 1) {
							vt[i - 1] = Integer.valueOf(tempstring[1]).intValue();
						}else {
							vt[i - 1] = 0;
						}
						if (tempstring.length > 2) {
							vn[i - 1] = Integer.valueOf(tempstring[2]).intValue();
						}else {
							vn[i - 1] = 0;
						}
					}
					model.face.add(v);
					model.facetex.add(vt);
					model.facenorm.add(vn);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if (scanner != null) scanner.close();
		model.glcall = GL11.glGenLists(1);
		
		GL11.glNewList(model.glcall, GL11.GL_COMPILE);
		for (int i = 0; i < model.face.size(); i++) {
			int[] tempfaces = (int[]) (model.face.get(i));
			int[] tempfacesnorms = (int[]) (model.facenorm.get(i));
			int[] tempfacestexs = (int[]) (model.facetex.get(i));
			
			int polytype;
			if (tempfaces.length == 3) {
				polytype = GL11.GL_TRIANGLES;
			}else if (tempfaces.length == 4) {
				polytype = GL11.GL_QUADS;
			}else {
				polytype = GL11.GL_POLYGON;
			}
			GL11.glBegin(polytype);
			
			for (int w = 0; w < tempfaces.length; w++) {
				if (tempfacesnorms[w] != 0) {
					float normtempx = ((float[]) model.normal.get(tempfacesnorms[w] - 1))[0];
					float normtempy = ((float[]) model.normal.get(tempfacesnorms[w] - 1))[1];
					float normtempz = ((float[]) model.normal.get(tempfacesnorms[w] - 1))[2];
					GL11.glNormal3f(normtempx, normtempy, normtempz);
				}
				
				if (tempfacestexs[w] != 0) {
					float textempx = ((float[]) model.tex.get(tempfacestexs[w] - 1))[0];
					float textempy = ((float[]) model.tex.get(tempfacestexs[w] - 1))[1];
					float textempz = ((float[]) model.tex.get(tempfacestexs[w] - 1))[2];
					GL11.glTexCoord3f(textempx, 1f - textempy, textempz);
				}
				
				float tempx = ((float[]) model.vertex.get(tempfaces[w] - 1))[0];
				float tempy = ((float[]) model.vertex.get(tempfaces[w] - 1))[1];
				float tempz = ((float[]) model.vertex.get(tempfaces[w] - 1))[2];
				GL11.glVertex3f(tempx, tempy, tempz);
			}
			GL11.glEnd();
		}
		GL11.glEndList();
		return model;
	}
	
	public float getWidth() {
		return right - left;
	}
	
	public float getHeight() {
		return top - bottom;
	}
	
	public float getDepth() {
		return near - far;
	}
	
	// the below methods are part of a wrapper for 3D modelling, and adds some extra support/features.
	
	public void draw() {
		if (pushed) {
			GL11.glCallList(glcall);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	private boolean pushed = false;
	
	public void push() {
		if (pushed) return;
		GL11.glPushMatrix();
		pushed = true;
	}
	
	public void pop() {
		GL11.glPopMatrix();
		pushed = false;
	}
	
	public void scale(float x, float y, float z) {
		if (pushed) GL11.glScalef(x, y, z);
	}
	
	public void bind(ImageRenderer.Texture texture) {
		if (pushed) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id);
		}
	}
	
	public void bind(String path) {
		ImageRenderer.Texture texture = ImageRenderer.ins.getTexture(path);
		if (pushed && texture != null) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id);
		}
	}
	
	public void rotate(float x, float y, float z) {
		if (pushed) {
			if (x != 0F) GL11.glRotatef(x, 1F, 0F, 0F);
			if (y != 0F) GL11.glRotatef(y, 0F, 1F, 0F);
			if (z != 0F) GL11.glRotatef(z, 0F, 0F, 1F);
		}
	}
	
	public void translate(float x, float y, float z) {
		if (pushed) GL11.glTranslatef(x, y, -z);
	}
}
