//Write sounds to file
(
var g, o, samplePath, wavFile, oscFile;
g = [
	[0, [\s_new, \NRTsine, 1000, 0, 0, \freq, 440]],
	[1, [\n_free, 1000]],
	[1, [\s_new, \NRTsine, 1001, 0, 0, \freq, 660]],
	[2, [\n_free, 1001]],
	[2, [\s_new, \NRTsine, 1002, 0, 0, \freq, 880],
		[\s_new, \NRTsine, 1003, 0, 0, \freq, 220]],
	[3, [\n_free, 1002], [\n_free, 1003]],
	[4, [\c_set, 0, 0]]
];
samplePath = thisProcess.nowExecutingPath.dirname;
oscFile = samplePath +/+ "christmas_tree.osc";
wavFile = samplePath +/+ "christmas_tree.wav";
o = ServerOptions.new.numOutputBusChannels = 2; // stereo output
Score.recordNRT(g, oscFile, wavFile, options: o); // synthesize
)
