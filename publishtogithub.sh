#!/bin/bash

basedir=out/publish
tmpdir=tmp
rootdir=`pwd`

rm -rf $tmpdir
mkdir -p $tmpdir

if ! test -d $tmpdir ; then
	echo "Invalid tmpdir! $tmpdir."
	exit 1
fi

if ! test -f $basedir/c/startpage.html ; then
	echo "Unable to find startpage.html - in $basedir - invalid publish run?!"
	exit 1
fi

#git clone --single-branch -b gh-pages git@github.com:dc2f/DC2F.git $tmpdir/gh-pages
git clone -b gh-pages git@github.com:dc2f/DC2F.git $tmpdir/gh-pages

export GIT_WORK_TREE=$tmpdir/gh-pages/
export GIT_DIR=$tmpdir/gh-pages/.git
rm -rf $tmpdir/gh-pages/*

cp -a $basedir/* $tmpdir/gh-pages/

git add -A

git commit -m "fresh publish run. `date`"
git push
