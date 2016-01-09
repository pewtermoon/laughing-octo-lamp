//Write sounds to file
(
var g, o, samplePath, wavFile, oscFile;
g = [
    [0.1, [\s_new, \NRTsine, 1000, 0, 0, \freq, 440]],
    [0.2, [\s_new, \NRTsine, 1001, 0, 0, \freq, 660]],
    [0.3, [\s_new, \NRTsine, 1002, 0, 0, \freq, 220]],
    [1, [\c_set, 0, 0]]
    ];
samplePath = thisProcess.nowExecutingPath.dirname;
oscFile = samplePath +/+ "christmas_tree.osc";
wavFile = samplePath +/+ "christmas_tree.wav";
o = ServerOptions.new.numOutputBusChannels = 2; // stereo output
Score.recordNRT(g, oscFile, wavFile, options: o); // synthesize
)
