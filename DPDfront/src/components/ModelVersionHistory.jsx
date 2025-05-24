// ModelVersionHistory.jsx
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { addModeldataVersion } from '../Redux/Modeldata/ModeldataSlice';
import AddVersionCard from './AddVersionCard';

const ModelVersionHistory = ({ model, onClose }) => {
  const dispatch = useDispatch();
  const { loading } = useSelector(state => state.modeldata);

  const [showAddVersionCard, setShowAddVersionCard] = useState(false);

  if (!model) return null;

  const handleAddVersion = async ({ file, description }) => {
    try {
      await dispatch(addModeldataVersion({
        modeldataId: model.id,
        file,
        description,
      })).unwrap();

      setShowAddVersionCard(false);
    } catch (err) {
      console.error('Failed to add new version:', err);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center z-50">
      <div className="relative w-full max-w-5xl h-[90vh] bg-white rounded-2xl shadow-2xl overflow-hidden flex flex-col border border-gray-300">

        {/* Close Button */}
        <button
          className="absolute top-4 right-4 text-gray-600 hover:text-red-600 text-2xl"
          onClick={onClose}
          aria-label="Close version history"
        >
          &times;
        </button>

        {/* Header and Add Button */}
        <div className="px-6 pt-6 mx-10 pb-4 border-b border-gray-200 flex justify-between items-center relative">
          <h2 className="text-3xl font-semibold text-gray-800">
            Version History for {model.name}
          </h2>
          <button
            onClick={() => setShowAddVersionCard(prev => !prev)}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
            aria-expanded={showAddVersionCard}
            aria-controls="add-version-card"
          >
            {showAddVersionCard ? 'Close Form' : 'Push New Version'}
          </button>

          {/* Render AddVersionCard floating over content */}
          {showAddVersionCard && (
            <AddVersionCard
              onSubmit={handleAddVersion}
              onCancel={() => setShowAddVersionCard(false)}
              loading={loading}
            />
          )}
        </div>

        {/* Version List */}
        <div className="px-6 py-4 overflow-y-auto flex-1">
          <ul className="space-y-4">
            {model.versions.map(version => (
              <li
                key={version.id}
                className="border p-4 rounded-lg shadow-sm hover:shadow-md transition"
              >
                <p className="text-lg font-medium">
                  Version #{version.versionNumber}{' '}
                  <span className="text-sm text-gray-500">
                    ({version.versionLabel || 'N/A'})
                  </span>
                </p>

                <p className="text-sm text-gray-600 mt-1">
                  Uploaded: {new Date(version.timestamp).toLocaleString()}
                </p>

                <p className="text-sm text-blue-700 mt-1">
                  File: {version.filePath?.split('\\').pop()}
                </p>

                {version.description && (
                  <p className="text-sm text-gray-700 mt-1">
                    Description: {version.description}
                  </p>
                )}

                <div className="mt-2">
                  <a
                    href={`http://localhost:8080/api/modeldata/versions/${version.id}/download`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="inline-block text-sm text-white bg-blue-600 hover:bg-blue-700 px-3 py-1 rounded"
                  >
                    Download
                  </a>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ModelVersionHistory;
