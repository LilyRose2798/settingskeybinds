import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.FabricProject;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.maven.MavenId;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends FabricProject {
    private static final String FABRIC_API_GROUP_ID = String.join(".", FabricMaven.GROUP_ID, "fabric-api");

    @Override
    public String getMcVersion() {
        return "1.18.1";
    }

    @Override
    public MappingTree createMappings() {
        return Yarn.ofMaven(FabricMaven.URL, FabricMaven.yarn("1.18.1+build.7")).tree;
    }

    @Override
    public FabricLoader getLoader() {
        return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.12.12"));
    }

    @Override
    public String getModId() {
        return "settingskeybinds";
    }

    @Override
    public String getVersion() {
        return "2.3.0";
    }

    @Override
    public int getJavaVersion() {
        return 17;
    }

    @Override
    public void getModDependencies(ModDependencyCollector d) {
        d.addMaven(FabricMaven.URL, new MavenId(FABRIC_API_GROUP_ID, "fabric-key-binding-api-v1", "1.0.9+d7c144a83a"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven(FabricMaven.URL, new MavenId(FABRIC_API_GROUP_ID, "fabric-lifecycle-events-v1", "1.4.12+d7c144a83a"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven(FabricMaven.URL, new MavenId(FABRIC_API_GROUP_ID, "fabric-resource-loader-v0", "0.4.13+d7c144a83a"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven(FabricMaven.URL, new MavenId(FABRIC_API_GROUP_ID, "fabric-screen-api-v1", "1.0.8+d7c144a83a"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven(FabricMaven.URL, new MavenId(FABRIC_API_GROUP_ID, "fabric-api-base", "0.4.2+d7c144a83a"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven("https://maven.shedaniel.me/", new MavenId("me.shedaniel.cloth", "cloth-config-fabric", "6.1.48"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven("https://maven.shedaniel.me/", new MavenId("me.shedaniel.cloth", "basic-math", "0.6.0"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven("https://maven.terraformersmc.com/releases/", new MavenId("com.terraformersmc", "modmenu", "3.0.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
    }
}
