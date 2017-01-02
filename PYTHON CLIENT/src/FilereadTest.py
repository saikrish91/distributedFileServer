import os


def chunkFile(file):
    fileChunk = []
    with open(file, "rb") as fileContent:
        data = fileContent.read(1024 * 1024)
        while data:
            fileChunk.append(data)
            data = fileContent.read(1024 * 1024)
    return fileChunk


f = 'C:\\Users\\mukul\\git\\fluffyglobal\\DSC_0607.jpg'
count =0

chunks = chunkFile(f)
leng = len(chunks)
print("%d"%leng)

