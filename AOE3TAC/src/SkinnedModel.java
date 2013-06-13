import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.ardor3d.extension.animation.skeletal.AnimationManager;
import com.ardor3d.extension.animation.skeletal.SkinnedMesh;
import com.ardor3d.extension.animation.skeletal.SkinnedMeshCombineLogic;
import com.ardor3d.extension.animation.skeletal.blendtree.SimpleAnimationApplier;
import com.ardor3d.extension.animation.skeletal.clip.AnimationClip;
import com.ardor3d.extension.animation.skeletal.state.loader.InputStore;
import com.ardor3d.extension.animation.skeletal.util.MissingCallback;
import com.ardor3d.extension.model.collada.jdom.ColladaImporter;
import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;
import com.ardor3d.extension.model.util.nvtristrip.NvTriangleStripper;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.hint.DataMode;
import com.ardor3d.scenegraph.visitor.Visitor;
import com.ardor3d.util.geom.MeshCombiner;

public class SkinnedModel {
	
	private static HashMap<String, SkinnedModel> skin = new HashMap<>();
	private static final ColladaImporter importer;
	private ColladaStorage storage = null;
	private SkinnedMesh mesh = null;
	private Node node = null;
	private AnimationManager anim = null;
	private HashMap<String, AnimationClip> anims = new HashMap<>();
	
	static {
		importer = new ColladaImporter();
		importer.setOptimizeMeshes(true);
	}
	
	private SkinnedModel(String path) {
		File file = new File(Start.path + "model\\" + path + ".dae");
		if (file.exists()) {
			try {
				storage = importer.load(file.getPath());
				node = storage.getScene();
				mesh = (SkinnedMesh) MeshCombiner.combine(node, new SkinnedMeshCombineLogic());
				mesh.acceptVisitor(new Visitor() {
					@Override
					public void visit(Spatial spatial) {
						if (spatial instanceof SkinnedMesh) {
							SkinnedMesh spat = (SkinnedMesh) spatial;
							spat.recreateWeightAttributeBuffer();
							spat.recreateJointAttributeBuffer();
						}
					}
				}, true);
				NvTriangleStripper stripper = new NvTriangleStripper();
				stripper.setReorderVertices(true);
				mesh.acceptVisitor(stripper, true);
				// CullState cullState = new CullState();
				// cullState.setCullFace(Face.Back);
				// mesh.setRenderState(cullState);
				mesh.getSceneHints().setDataMode(DataMode.VBO);
				mesh.setUseGPU(true);
				anim = new AnimationManager(Timer.timer, mesh.getCurrentPose());
				SimpleAnimationApplier applier = new SimpleAnimationApplier();
				anim.setApplier(applier);
				InputStore input = new InputStore();
				input.getClips().setMissCallback(new MissingCallback<String, AnimationClip>() {
					public AnimationClip getValue(String key) {
						if (!anims.containsKey(key)) {
							try {
								ColladaStorage tstor = importer.load(Start.path + "anim\\" + key.replace("/", "\\") + ".dae");
								anims.put(key, tstor.extractChannelsAsClip(key));
							}catch (IOException e) {
								e.printStackTrace();
							}
						}
						return anims.get(key);
					}
				});
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static SkinnedModel get(String path) {
		if (skin.containsKey(path.replace("/", "\\"))) return skin.get(path.replace("/", "\\"));
		return new SkinnedModel(path.replace("/", "\\"));
	}
	
}
