#!/bin/bash
YARN_BIN=`which yarn`
echo "Running yarn $1"
$YARN_BIN install
$YARN_BIN $1
