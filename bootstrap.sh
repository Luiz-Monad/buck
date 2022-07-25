#! /bin/bash

ant
./bin/buck build --show-output buck
./bin/buck build --config java.target_level=11 --config java.source_level=11 buck --show-output
cp buck-out/gen/ce9b6f2e/programs/buck.pex ./buck
rm -rf ./ant-out
rm -rf ./buck-out
