const FILE_EXTENSION = {
  js: 'javascript',
  md: 'markdown',
  kt: 'kotlin',
  kts: 'kotlin',
  gradle: 'groovy',
  gitignore: 'git',
  java: 'java',
  xml: 'xml',
  properties: 'properties',
  groovy: 'groovy',
}

const getLanguage = file => {
  if (!file.includes(`.`)) {
    return null
  }
  const extension = file.split(`.`).pop()
  if (FILE_EXTENSION.hasOwnProperty(extension)) {
    return FILE_EXTENSION[extension]
  }
  return null
}

export const createTree = (files, path, fileName, zip, onInitialLoad) => {
  return new Promise((resolve, reject) => {
    const recursive = (pfiles, ppath, pfileName, pzip, pdepth) => {
      const type = pfiles[ppath].dir ? 'folder' : 'file'
      const item = {
        type: type,
        filename: pfileName,
        path: '/' + ppath,
        hidden: pdepth === 1 && type === 'folder' ? true : null,
      }
      if (type === 'folder') {
        const children = []
        pzip.folder(ppath).forEach((relativePath, file) => {
          const pathArray = relativePath.split('/')
          if (pathArray.length === 1 || (file.dir && pathArray.length === 2)) {
            children.push(
              recursive(
                pfiles,
                ppath + relativePath,
                relativePath,
                pzip,
                pdepth + 1
              )
            )
          }
        })
        item.children = children
        item.filename = pfileName.substring(0, pfileName.length - 1)
      } else {
        item.language = getLanguage(item.filename)
        if (item.language) {
          pfiles[ppath].async('string').then(content => {
            item.content = content
          })
        }
      }
      return item
    }
    const tree = recursive(files, path, fileName, zip, 0)
    const selected = tree.children.find(
      item =>
        ['pom.xml', 'build.gradle', 'build.gradle.kts'].indexOf(item.filename) >
        -1
    )
    if (selected) {
      files[selected.path.substring(1)].async('string').then(content => {
        selected.content = content
        resolve({ tree: tree, selected: selected })
      })
    } else {
      resolve({ tree: tree, selected: null })
    }
  })
}

export const findRoot = zip => {
  let root = Object.keys(zip.files).filter(filename => {
    let pathArray = filename.split('/')
    if (zip.files[filename].dir && pathArray.length === 2) {
      return true
    }
    return false
  })[0]
  return root.substring(0, root.length - 1)
}
