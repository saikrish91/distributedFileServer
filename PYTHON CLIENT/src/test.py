def read_in_chunks(file_object, chunk_size=1024 * 1024):
    """Lazy function (generator) to read a file piece by piece.
    chunk size: 1mb."""

    while True:
        data = file_object.read(chunk_size)
        if not data:
            break
        yield data


f = open('C:\\Users\\mukul\\git\\fluffyglobal\\DSC_0607.jpg')
i = -1
fileChunk = []
for piece in read_in_chunks(f):
    i = i + 1
    fileChunk.append(piece)
    print ("chunk %d" % i)

print("%d"%len(fileChunk))