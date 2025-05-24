import React from 'react';

const QAModelDataCard = ({ model, onSelect, onApprove, onReject }) => {
  if (!model) return null;

  const {
    id,
    name,
    createdAt,
    updatedAt,
    currentVersion,
    status,
    appuser,
  } = model;

  const getFileName = (filePath) => {
    if (!filePath) return 'N/A';
    return filePath.split(/[\\/]/).pop();
  };

  const username = appuser?.username || 'Unknown User';

  const getFileUrl = (filePath) => {
    if (!filePath) return '#';
    return filePath.startsWith('http') ? filePath : `http://localhost:8080/${filePath}`;
  };

  const isActionable = status === 'UNDER_SCRUTINY';

  // Status color mapping for badge
  const statusColors = {
    APPROVED: 'bg-green-600 text-green-100',
    SENT_BACK: 'bg-red-600 text-red-100',
    UNDER_SCRUTINY: 'bg-yellow-500 text-yellow-900',
    DEFAULT: 'bg-gray-700 text-gray-300',
  };

  const statusClass = statusColors[status] || statusColors.DEFAULT;

  return (
    <div
  onClick={() => onSelect(model.id)}
  className={`flex w-full rounded-lg shadow-md cursor-pointer overflow-hidden
    border
    ${status === 'APPROVED' ? 'border-green-600 bg-green-900' : 'border-gray-700 bg-gray-900'}
    hover:shadow-xl transition-shadow duration-300 text-white
  `}
  style={{ minHeight: 140 }}
>


      {/* Left side - main info and version */}
      <div className="flex flex-col flex-grow p-6 gap-3">
        <h2 className="text-xl font-extrabold tracking-wide truncate">{name}</h2>

        <div className="flex gap-8 text-sm text-gray-300">
          <div>
            <span className="font-semibold">Submitted By:</span> {username}
          </div>
          <div>
            <span className="font-semibold">Created:</span>{' '}
            {new Date(createdAt).toLocaleDateString()}
          </div>
          <div>
            <span className="font-semibold">Updated:</span>{' '}
            {new Date(updatedAt).toLocaleDateString()}
          </div>
        </div>

        {currentVersion && (
          <div className="bg-gray-800 rounded-md border border-gray-700 p-3 text-xs max-w-md overflow-hidden">
            <h3 className="font-semibold mb-1 border-b border-gray-600 pb-0.5">
              Current Version
            </h3>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <span className="font-medium">Version #:</span> {currentVersion.versionNumber}
              </div>
              <div>
                <span className="font-medium">Label:</span>{' '}
                {currentVersion.versionLabel || 'N/A'}
              </div>
              <div>
                <span className="font-medium">Timestamp:</span>{' '}
                {new Date(currentVersion.timestamp).toLocaleString()}
              </div>
              <div className="truncate">
                <span className="font-medium">File:</span>{' '}
                <a
                  href={getFileUrl(currentVersion.filePath)}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-blue-400 hover:underline break-words"
                  onClick={(e) => e.stopPropagation()}
                  title="Open file in new tab"
                >
                  {getFileName(currentVersion.filePath)}
                </a>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Right side - status, actions */}
      <div className="flex flex-col justify-between p-4 w-48 bg-gray-800 border-l border-gray-700">
        <div>
          <div
            className={`inline-block px-3 py-1 rounded-full text-center font-semibold tracking-wide ${statusClass}`}
          >
            {status}
          </div>
        </div>

        {isActionable && (
          <div className="flex flex-col gap-3 mt-4">
            <button
              onClick={(e) => {
                e.stopPropagation();
                onApprove(id);
              }}
              className="w-full px-3 py-2 bg-green-600 rounded hover:bg-green-700 font-semibold transition-colors"
            >
              Approve
            </button>

            <button
              onClick={(e) => {
                e.stopPropagation();
                onReject(id);
              }}
              className="w-full px-3 py-2 bg-red-600 rounded hover:bg-red-700 font-semibold transition-colors"
            >
              Reject
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default QAModelDataCard;
