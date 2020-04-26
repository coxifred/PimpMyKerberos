package pimpmykerberos.deeplearning;

public class DeepLearning {

	static private DeepLearning instance;

//	
//	protected static int height=32;
//    protected static int width=32;
//
//    protected static int channels = 3;
//    protected static int batchSize=150;// tested 50, 100, 200
//    protected static long seed = 123;
//    protected static Random rng = new Random(seed);
//    protected static int iterations = 1;
//    protected static int nEpochs = 150; // tested 50, 100, 200
//    protected static double splitTrainTest = 0.8;
//    protected static boolean save = true;
//    private int numLabels=5;
//
//
	public static DeepLearning getInstance() {
		if (instance == null)
			instance = new DeepLearning();
		return instance;
	}

//	
	public void startUI() {
//		 MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
//	                .seed(seed)
//	                .activation(Activation.RELU)
//	                
//	                .weightInit(WeightInit.XAVIER)
//	                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//	                .updater(Updater.ADAM)
//	                .list()
//	                .layer(0, convInit("cnn1", channels, 32 ,  new int[]{5, 5}, new int[]{1, 1}, new int[]{0, 0}, 0))
//	                .layer(1, maxPool("maxpool1", new int[]{2,2}))
//	                .layer(2, conv3x3("cnn2", 64, 0))
//	                .layer(3, conv3x3("cnn3", 64,1))
//	                .layer(4, maxPool("maxpool2", new int[]{2,2}))
//	                .layer(5, new DenseLayer.Builder().activation(Activation.RELU)
//	                        .nOut(512).dropOut(0.5).build())
//	                .layer(6, new OutputLayer.Builder(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
//	                        .nOut(numLabels)
//	                        .nIn(numLabels)
//	                        .activation(Activation.SOFTMAX)
//	                        .build())
//	                
//	                .setInputType(InputType.convolutional(height, width, channels))
//	                .build();
//	        MultiLayerNetwork network = new MultiLayerNetwork(conf);
//		
//	    //Initialize the user interface backend
//	    UIServer uiServer = UIServer.getInstance();
//	   
//	    //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
//	    StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later
//
//	    //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
//	    uiServer.attach(statsStorage);
//
//	    //Then add the StatsListener to collect this information from the network, as it trains
//	    network.setListeners(new StatsListener(statsStorage));
	}
//	
//	   private ConvolutionLayer convInit(String name, int in, int out, int[] kernel, int[] stride, int[] pad, double bias) {
//	        return new ConvolutionLayer.Builder(kernel, stride, pad).name(name).nIn(in).nOut(out).biasInit(bias).build();
//	    }
//
//	    private ConvolutionLayer conv3x3(String name, int out, double bias) {
//	        return new ConvolutionLayer.Builder(new int[]{3,3}, new int[] {1,1}, new int[] {1,1}).name(name).nOut(out).biasInit(bias).build();
//	    }
//
//
//	    private SubsamplingLayer maxPool(String name, int[] kernel) {
//	        return new SubsamplingLayer.Builder(kernel, new int[]{2,2}).name(name).build();
//	    }
}
